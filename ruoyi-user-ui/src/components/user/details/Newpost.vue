<template>
  <div class="newPost">
    <div v-if="loading" class="loading">加载中...</div>

    <template v-else>
      <div class="header">
        <h2>发布新帖子</h2>
        <p class="subtitle">分享你的想法和经验</p>
      </div>

      <el-form
          ref="postFormRef"
          :model="postForm"
          :rules="formRules"
          label-width="100px"
          class="post-form"
      >
        <!-- 帖子标题 -->
        <el-form-item label="帖子标题" prop="postTitle">
          <el-input
              v-model="postForm.postTitle"
              placeholder="请输入帖子标题(最多100字符)"
              maxlength="100"
              show-word-limit
              clearable></el-input>
        </el-form-item>

        <!-- 游戏类型选择 -->
        <el-form-item label="游戏类型" prop="gameTypeId">
          <el-select
              v-model="postForm.gameTypeId"
              placeholder="请选择游戏类型"
              @change="handleGameTypeChange"
              style="width: 100%"
          >
            <el-option
                v-for="type in gameTypeList"
                :key="type.typeId"
                :label="type.typeName"
                :value="type.typeId"
            />
          </el-select>
        </el-form-item>

        <!-- 游戏选择 -->
        <el-form-item label="选择游戏" prop="gameId">
          <el-select
              v-model="postForm.gameId"
              placeholder="请选择游戏"
              @change="handleGameChange"
              style="width: 100%"
              :disabled="!postForm.gameTypeId"
          >
            <el-option
                v-for="game in filteredGames"
                :key="game.gameId"
                :label="game.gameName"
                :value="game.gameId"
            />
          </el-select>
        </el-form-item>

        <!-- 版块选择 -->
        <el-form-item label="选择版块" prop="sectionId">
          <el-select
              v-model="postForm.sectionId"
              placeholder="请选择版块"
              style="width: 100%"
              :disabled="!postForm.gameId"
          >
            <el-option
                v-for="section in filteredSections"
                :key="section.sectionId"
                :label="section.sectionName"
                :value="section.sectionId"
            />
          </el-select>
        </el-form-item>

        <!-- 帖子内容(富文本编辑器) -->
        <el-form-item label="帖子内容" prop="postContent">
          <div class="editor-container">
            <div ref="editorRef" class="editor"></div>
            <div class="content-tip">
              支持富文本编辑，可插入图片。纯文本最多10000字符（图片不计入字数）
              <span class="char-count" :class="{'over-limit': textCharCount > 10000}">
                当前字数：{{ textCharCount }}/10000
              </span>
            </div>
          </div>
        </el-form-item>

        <!-- 操作按钮 -->
        <el-form-item class="form-actions">
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" :loading="publishing" @click="submitPost">
            {{ publishing ? '发布中...' : '发布帖子' }}
          </el-button>
        </el-form-item>
      </el-form>
    </template>
  </div>
</template>

<script setup>
import {computed, onBeforeUnmount, onMounted, reactive, ref, watch} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {useRoute, useRouter} from 'vue-router'
import API_URLS from '@/api/apiUrls.js'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const postFormRef = ref()
const editorRef = ref()

let quillEditor = null

const gameTypeList = ref([])
const gameList = ref([])
const filteredGames = ref([])
const filteredSections = ref([])

const postForm = reactive({
  postTitle: '',
  postContent: '',
  sectionId: null,
  gameTypeId: null,
  gameId: null
})

const sectionList = ref([])

/**
 * 计算纯文本字符数（不包含base64图片和HTML标签）
 * 与后端逻辑保持一致
 */
function calculatePureTextLength(htmlContent) {
  if (!htmlContent) return 0

  // 第一步：移除所有base64图片
  let withoutBase64 = htmlContent.replace(
      /<img[^>]+src="data:image\/\w+;base64,[^"]*"[^>]*>/gi,
      ''
  )

  // 第二步：移除所有HTML标签
  let plainText = withoutBase64.replace(/<[^>]+>/g, '')

  // 第三步：解码HTML实体
  plainText = plainText
      .replace(/&nbsp;/g, ' ')
      .replace(/&lt;/g, '<')
      .replace(/&gt;/g, '>')
      .replace(/&amp;/g, '&')
      .replace(/&quot;/g, '"')
      .replace(/&#39;/g, "'")

  // 第四步：去除前后空格并返回长度
  return plainText.trim().length
}

// 计算纯文本字符数（不包括图片标签）
const textCharCount = computed(() => {
  return calculatePureTextLength(postForm.postContent)
})

// 内容验证器
function validateContent(rule, value, callback) {
  const pureTextLen = calculatePureTextLength(value)

  if (!value || value.trim() === '' || value === '<p><br></p>') {
    callback(new Error('请输入帖子内容'))
  } else if (pureTextLen > 10000) {
    callback(new Error('纯文本内容不能超过10000字符'))
  } else {
    // ✅ 不再检查 value.length（因为可能包含 base64 图片）
    callback()
  }
}


const formRules = {
  postTitle: [
    {required: true, message: '请输入帖子标题', trigger: 'blur'},
    {min: 1, max: 100, message: '标题长度应在1-100字符之间', trigger: 'blur'}
  ],
  sectionId: [
    {required: true, message: '请选择发布版块', trigger: 'change'}
  ],
  postContent: [
    {required: true, message: '请输入帖子内容', trigger: 'blur'},
    {validator: validateContent, trigger: 'blur'}
  ],
  gameTypeId: [
    {required: true, message: '请选择游戏类型', trigger: 'change'}
  ],
  gameId: [
    {required: true, message: '请选择游戏', trigger: 'change'}
  ]
}

const publishing = ref(false)

watch(() => route.query.sectionId, (newSectionId) => {
  if (newSectionId && !loading.value) {
    autoFillFromSectionId(parseInt(newSectionId))
  }
}, {immediate: true})

async function loadGameTypes() {
  try {
    const result = await apiRequest(API_URLS.getAllGameTypes())
    if (result) gameTypeList.value = result.data
  } catch (error) {
    console.error('加载游戏类型失败:', error)
  }
}

async function loadGames() {
  try {
    const result = await apiRequest(API_URLS.getAllGames())
    if (result) gameList.value = result.data
  } catch (error) {
    console.error('加载游戏列表失败:', error)
  }
}

async function loadSections() {
  try {
    const result = await apiRequest(API_URLS.getAllSections())
    if (result) sectionList.value = result.data
  } catch (error) {
    console.error('加载版块列表失败:', error)
  }
}

async function autoFillFromSectionId(sectionId) {
  try {
    const targetSection = sectionList.value.find(section => section.sectionId === sectionId)
    if (!targetSection) return

    const targetGame = gameList.value.find(game => game.gameId === targetSection.gameId)
    if (!targetGame) return

    const targetGameType = gameTypeList.value.find(type => type.typeId === targetGame.gameTypeId)
    if (!targetGameType) return

    postForm.gameTypeId = targetGameType.typeId
    handleGameTypeChange(targetGameType.typeId)

    setTimeout(() => {
      postForm.gameId = targetGame.gameId
      handleGameChange(targetGame.gameId)

      setTimeout(() => {
        postForm.sectionId = sectionId
        ElMessage.success(`已自动选择：${targetGameType.typeName} - ${targetGame.gameName} - ${targetSection.sectionName}`)
      }, 100)
    }, 100)

  } catch (error) {
    console.error('自动填充失败:', error)
    ElMessage.warning('自动填充失败，请手动选择')
  }
}

const handleGameTypeChange = (typeId) => {
  if (postForm.gameId && !gameList.value.find(game => game.gameId === postForm.gameId && game.gameTypeId === typeId)) {
    postForm.gameId = null
  }
  if (postForm.sectionId && !sectionList.value.find(section => section.sectionId === postForm.sectionId)) {
    postForm.sectionId = null
  }

  filteredGames.value = gameList.value.filter(game => game.gameTypeId === typeId)
  filteredSections.value = []
}

const handleGameChange = (gameId) => {
  if (postForm.sectionId && !sectionList.value.find(section => section.sectionId === postForm.sectionId && section.gameId === gameId)) {
    postForm.sectionId = null
  }

  filteredSections.value = sectionList.value.filter(section => section.gameId === gameId)
}

const getAuthData = () => {
  const accessToken = localStorage.getItem('accessToken')
  const userInfoStr = localStorage.getItem('userInfo')

  if (!accessToken || !userInfoStr) {
    ElMessage.error('请先登录')
    router.push('/login')
    return null
  }

  try {
    return {
      token: accessToken,
      userInfo: JSON.parse(userInfoStr)
    }
  } catch {
    ElMessage.error('登录信息无效，请重新登录')
    router.push('/login')
    return null
  }
}

const apiRequest = async (url, options = {}) => {
  const authData = getAuthData()
  if (!authData) return null

  const response = await fetch(url, {
    ...options,
    headers: {
      'Authorization': `Bearer ${authData.token}`,
      'Content-Type': 'application/json',
      ...options.headers
    }
  })

  const result = await response.json()
  if (result.code !== 200) {
    throw new Error(result.message || '请求失败')
  }
  return result
}

// 初始化富文本编辑器
function initEditor() {
  if (typeof Quill === 'undefined') {
    const link = document.createElement('link')
    link.rel = 'stylesheet'
    link.href = 'https://cdn.quilljs.com/1.3.6/quill.snow.css'
    document.head.appendChild(link)

    const script = document.createElement('script')
    script.src = 'https://cdn.quilljs.com/1.3.6/quill.js'
    script.onload = () => {
      createEditor()
    }
    document.head.appendChild(script)
  } else {
    createEditor()
  }
}

function createEditor() {
  quillEditor = new Quill(editorRef.value, {
    theme: 'snow',
    placeholder: '请输入帖子内容，可以插入图片...',
    modules: {
      toolbar: {
        container: [
          ['bold', 'italic', 'underline', 'strike'],
          ['blockquote', 'code-block'],
          [{'header': 1}, {'header': 2}],
          [{'list': 'ordered'}, {'list': 'bullet'}],
          [{'script': 'sub'}, {'script': 'super'}],
          [{'indent': '-1'}, {'indent': '+1'}],
          [{'size': ['small', false, 'large', 'huge']}],
          [{'header': [1, 2, 3, 4, 5, 6, false]}],
          [{'color': []}, {'background': []}],
          [{'align': []}],
          ['image'], // 图片按钮
          ['clean']
        ],
        handlers: {
          image: imageHandler // 自定义图片处理
        }
      }
    }
  })

  // 监听内容变化
  quillEditor.on('text-change', () => {
    postForm.postContent = quillEditor.root.innerHTML
  })
}

// 自定义图片处理函数
function imageHandler() {
  const input = document.createElement('input')
  input.setAttribute('type', 'file')
  input.setAttribute('accept', 'image/*')

  input.onchange = async () => {
    const file = input.files[0]
    if (!file) return

    // 验证文件大小
    if (file.size > 10 * 1024 * 1024) {
      ElMessage.error('图片大小不能超过10MB')
      return
    }

    // 验证文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.error('请选择图片文件')
      return
    }

    try {
      // 转换为base64
      const reader = new FileReader()
      reader.onload = (e) => {
        const range = quillEditor.getSelection(true)
        // 插入base64图片到编辑器
        quillEditor.insertEmbed(range.index, 'image', e.target.result)
        // 移动光标到图片后面
        quillEditor.setSelection(range.index + 1)
      }
      reader.readAsDataURL(file)

      ElMessage.success('图片已插入')
    } catch (error) {
      console.error('图片插入失败:', error)
      ElMessage.error('图片插入失败')
    }
  }

  input.click()
}

/**
 * 提取并压缩HTML中的base64图片
 * 将base64图片转换为相对路径，减少传输数据量
 */
async function processContentBeforeSubmit(content, authHeader) {
  if (!content) return content

  const base64ImgPattern = /<img[^>]+src="(data:image\/(\w+);base64,([^"]+))"[^>]*>/gi
  const matches = [...content.matchAll(base64ImgPattern)]

  if (matches.length === 0) {
    return content
  }

  let processedContent = content
  let successCount = 0

  // 逐个处理base64图片
  for (let i = 0; i < matches.length; i++) {
    const match = matches[i]
    const fullMatch = match[0]
    const base64Data = match[3]
    const imageType = match[2]

    try {
      // 上传图片到后端
      const uploadResult = await apiRequest(API_URLS.uploadPostImage(), {
        method: 'POST',
        body: JSON.stringify({
          fileName: `post_${Date.now()}_${Math.random().toString(36).substring(2, 10)}_${i}.${imageType}`,
          base64Data: base64Data
        }),
        headers: {
          'Authorization': `Bearer ${getAuthData().token}`
        }
      })

      if (uploadResult && uploadResult.data) {
        // 替换为服务器返回的相对路径
        const relativePath = uploadResult.data
        const replacement = `<img src="${relativePath}" alt="post-image-${i}" />`
        processedContent = processedContent.replace(fullMatch, replacement)
        successCount++
      }
    } catch (error) {
      //console.error(`上传第${i + 1}张图片失败:`, error)
      //ElMessage.warning(`第${i + 1}张图片上传失败，将保留原图片`)
    }
  }

  return processedContent
}

// 提交帖子
async function submitPost() {
  try {
    await postFormRef.value.validate()
  } catch {
    return
  }

  const authData = getAuthData()
  if (!authData) return

  publishing.value = true

  try {
    ElMessage.info('正在处理图片...')

    // 先处理内容中的base64图片，转换为服务器路径
    const processedContent = await processContentBeforeSubmit(
        postForm.postContent,
        `Bearer ${authData.token}`
    )

    // 再次验证处理后的纯文本长度
    const pureTextLength = calculatePureTextLength(processedContent)

    if (pureTextLength > 10000) {
      ElMessage.error('纯文本内容超过10000字符限制')
      publishing.value = false
      return
    }

    // 提交处理后的内容（不含base64）
    const result = await apiRequest(API_URLS.createPost(), {
      method: 'POST',
      body: JSON.stringify({
        postTitle: postForm.postTitle,
        postContent: processedContent,
        sectionId: postForm.sectionId
      })
    })

    if (result) {
      ElMessage.success('帖子发布成功!')
      router.push('/')
    }
  } catch (error) {
    console.error('发布帖子失败:', error)
    if (error.message.includes('登录') || error.message.includes('认证')) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.clear()
      router.push('/login')
    } else {
      ElMessage.error(error.message || '发布失败，请稍后重试')
    }
  } finally {
    publishing.value = false
  }
}

// 重置表单
function resetForm() {
  ElMessageBox.confirm('确定要重置表单吗？所有内容将被清空。', '确认重置', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    postFormRef.value.resetFields()
    if (quillEditor) {
      quillEditor.setContents([])
    }
    postForm.postContent = ''
    filteredGames.value = []
    filteredSections.value = []
    ElMessage.success('表单已重置')
  })
}

// 初始化
const init = async () => {
  const authData = getAuthData()
  if (!authData) return

  try {
    await Promise.all([
      loadGameTypes(),
      loadGames(),
      loadSections()
    ])

    setTimeout(() => {
      initEditor()
    }, 100)

    const sectionId = route.query.sectionId
    if (sectionId) {
      setTimeout(() => {
        autoFillFromSectionId(parseInt(sectionId))
      }, 200)
    }

  } catch (error) {
    console.error('初始化失败:', error)
    ElMessage.error('初始化失败，请刷新页面重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  init()
})

onBeforeUnmount(() => {
  if (quillEditor) {
    quillEditor = null
  }
})
</script>
<style scoped>
.newPost {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

.loading {
  text-align: center;
  padding: 50px;
  color: #666;
  font-size: 16px;
}

.header {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.header h2 {
  color: #303133;
  margin-bottom: 8px;
  font-size: 24px;
  font-weight: 500;
}

.subtitle {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

.post-form {
  margin-top: 20px;
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.editor {
  height: 400px;
}

.content-tip {
  padding: 8px 12px;
  background: #f5f7fa;
  color: #909399;
  font-size: 12px;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.char-count {
  font-weight: 500;
  color: #606266;
}

.char-count.over-limit {
  color: #f56c6c;
  font-weight: 600;
}

.form-actions {
  text-align: center;
  margin-top: 30px;
}

.form-actions .el-button {
  min-width: 100px;
  margin: 0 10px;
}

/* 富文本编辑器样式 */
:deep(.ql-toolbar) {
  border-bottom: 1px solid #ccc;
  background: #f8f9fa;
}

:deep(.ql-container) {
  border: none;
  font-size: 14px;
  line-height: 1.6;
}

:deep(.ql-editor) {
  min-height: 350px;
  padding: 12px 15px;
}

:deep(.ql-editor.ql-blank::before) {
  color: #c0c4cc;
  font-style: normal;
}

:deep(.ql-editor img) {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 10px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .newPost {
    margin: 10px;
    padding: 15px;
  }

  .header h2 {
    font-size: 20px;
  }

  .form-actions .el-button {
    margin: 5px;
    min-width: 80px;
  }

  .editor {
    height: 300px;
  }

  .content-tip {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
}
</style>