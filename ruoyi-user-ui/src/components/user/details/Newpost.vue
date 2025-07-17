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
              placeholder="请输入帖子标题（最多100字符）"
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

        <!-- 图片上传 -->
        <el-form-item label="上传图片" prop="photo">
          <div class="upload-section">
            <div class="upload-area" @click="$refs.fileInput.click()">
              <input
                  ref="fileInput"
                  type="file"
                  @change="handleFileSelect"
                  accept="image/*"
                  style="display: none"
              />
              <div v-if="!imagePreview" class="upload-placeholder">
                <el-icon class="upload-icon">
                  <Plus/>
                </el-icon>
                <div class="upload-text">点击选择图片</div>
                <div class="upload-tip">支持 JPG、PNG、GIF 格式，大小不超过 5MB</div>
              </div>
              <div v-else class="image-preview">
                <img :src="imagePreview" alt="预览图片"/>
                <div class="image-overlay">
                  <el-button type="danger" size="small" @click.stop="removeImage">
                    删除图片
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>

        <!-- 帖子内容 -->
        <el-form-item label="帖子内容" prop="postContent">
          <div class="editor-container">
            <div ref="editorRef" class="editor"></div>
            <div class="content-tip">
              支持富文本编辑，最多10000字符
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
import {onBeforeUnmount, onMounted, reactive, ref, watch} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus} from '@element-plus/icons-vue'
import {useRoute, useRouter} from 'vue-router'
import API_URLS from '@/api/apiUrls.js'

// 路由
const router = useRouter()
const route = useRoute()

// 状态管理
const loading = ref(true)

// 表单引用
const postFormRef = ref()
const editorRef = ref()
const fileInput = ref()

// 富文本编辑器实例
let quillEditor = null

// 新增数据
const gameTypeList = ref([])
const gameList = ref([])
const filteredGames = ref([])
const filteredSections = ref([])

// 表单数据
const postForm = reactive({
  postTitle: '',
  postContent: '',
  sectionId: null,
  photo: '',
  gameTypeId: null,
  gameId: null
})

// 版块列表
const sectionList = ref([])

// 表单验证规则
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

// 图片相关
const imagePreview = ref('')
const selectedFile = ref(null)
const publishing = ref(false)

// 监听路由参数变化
watch(() => route.query.sectionId, (newSectionId) => {
  if (newSectionId && !loading.value) {
    autoFillFromSectionId(parseInt(newSectionId))
  }
}, {immediate: true})

// 新增方法
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

// 根据 sectionId 自动填充相关选择框
async function autoFillFromSectionId(sectionId) {
  try {
    // 查找对应的版块
    const targetSection = sectionList.value.find(section => section.sectionId === sectionId)
    if (!targetSection) {
      console.warn('未找到对应的版块:', sectionId)
      return
    }

    // 查找对应的游戏
    const targetGame = gameList.value.find(game => game.gameId === targetSection.gameId)
    if (!targetGame) {
      console.warn('未找到对应的游戏:', targetSection.gameId)
      return
    }

    // 查找对应的游戏类型
    const targetGameType = gameTypeList.value.find(type => type.typeId === targetGame.gameTypeId)
    if (!targetGameType) {
      console.warn('未找到对应的游戏类型:', targetGame.gameTypeId)
      return
    }

    // 按顺序填充选择框
    postForm.gameTypeId = targetGameType.typeId

    // 触发游戏类型变化，更新游戏列表
    handleGameTypeChange(targetGameType.typeId)

    // 延迟设置游戏ID，确保filteredGames已更新
    setTimeout(() => {
      postForm.gameId = targetGame.gameId

      // 触发游戏变化，更新版块列表
      handleGameChange(targetGame.gameId)

      // 延迟设置版块ID，确保filteredSections已更新
      setTimeout(() => {
        postForm.sectionId = sectionId

        // 显示自动填充成功的提示
        ElMessage.success(`已自动选择：${targetGameType.typeName} - ${targetGame.gameName} - ${targetSection.sectionName}`)
      }, 100)
    }, 100)

  } catch (error) {
    console.error('自动填充失败:', error)
    ElMessage.warning('自动填充失败，请手动选择')
  }
}

const handleGameTypeChange = (typeId) => {
  // 如果不是自动填充触发的，则清空后续选择
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
  // 如果不是自动填充触发的，则清空版块选择
  if (postForm.sectionId && !sectionList.value.find(section => section.sectionId === postForm.sectionId && section.gameId === gameId)) {
    postForm.sectionId = null
  }

  filteredSections.value = sectionList.value.filter(section => section.gameId === gameId)
}

// 获取认证数据
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

// API 请求封装
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

// 内容验证器
function validateContent(rule, value, callback) {
  if (!value || value.trim() === '' || value === '<p><br></p>') {
    callback(new Error('请输入帖子内容'))
  } else if (value.length > 10000) {
    callback(new Error('内容长度不能超过10000字符'))
  } else {
    callback()
  }
}

// 初始化富文本编辑器
function initEditor() {
  // 使用 CDN 加载 Quill 编辑器
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
    placeholder: '请输入帖子内容...',
    modules: {
      toolbar: [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{'header': 1}, {'header': 2}],
        [{'list': 'ordered'}, {'list': 'bullet'}],
        [{'script': 'sub'}, {'script': 'super'}],
        [{'indent': '-1'}, {'indent': '+1'}],
        [{'direction': 'rtl'}],
        [{'size': ['small', false, 'large', 'huge']}],
        [{'header': [1, 2, 3, 4, 5, 6, false]}],
        [{'color': []}, {'background': []}],
        [{'font': []}],
        [{'align': []}],
        ['clean'],
        ['link']
      ]
    }
  })

  // 监听内容变化
  quillEditor.on('text-change', () => {
    postForm.postContent = quillEditor.root.innerHTML
  })
}

// 文件选择处理
const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 5MB')
    return
  }

  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }

  selectedFile.value = file

  const reader = new FileReader()
  reader.onload = (e) => {
    imagePreview.value = e.target.result
  }
  reader.readAsDataURL(file)
}

// 删除图片
function removeImage() {
  imagePreview.value = ''
  selectedFile.value = null
  postForm.photo = ''
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

// 上传图片到服务器
async function uploadImage() {
  if (!selectedFile.value) return ''

  try {
    const authData = getAuthData()
    if (!authData) return ''

    const ext = selectedFile.value.name.split('.').pop()
    const fileName = `post_${Date.now()}_${Math.random().toString(36).substr(2, 9)}.${ext}`

    const reader = new FileReader()
    return new Promise((resolve, reject) => {
      reader.onload = async (e) => {
        try {
          const response = await fetch(API_URLS.uploadPostImage(), {
            method: 'POST',
            headers: {
              'Authorization': `Bearer ${authData.token}`,
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({
              fileName,
              base64Data: e.target.result
            })
          })

          const result = await response.json()
          if (result.code === 200) {
            resolve(`images/user/post/${fileName}`)
          } else {
            reject(new Error(result.message || '图片上传失败'))
          }
        } catch (error) {
          reject(error)
        }
      }
      reader.onerror = reject
      reader.readAsDataURL(selectedFile.value)
    })
  } catch (error) {
    throw new Error('图片上传失败: ' + error.message)
  }
}

// 提交帖子
async function submitPost() {
  // 验证表单
  try {
    await postFormRef.value.validate()
  } catch {
    return
  }

  // 检查登录状态
  const authData = getAuthData()
  if (!authData) return

  publishing.value = true

  try {
    // 上传图片（如果有）
    if (selectedFile.value) {
      postForm.photo = await uploadImage()
    }

    // 提交帖子数据
    const result = await apiRequest(API_URLS.createPost(), {
      method: 'POST',
      body: JSON.stringify({
        postTitle: postForm.postTitle,
        postContent: postForm.postContent,
        sectionId: postForm.sectionId,
        photo: postForm.photo
      })
    })

    if (result) {
      ElMessage.success('帖子发布成功!')
      // 跳转到帖子列表或帖子详情页
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
    removeImage()
    if (quillEditor) {
      quillEditor.setContents([])
    }
    postForm.postContent = ''

    // 重置筛选列表
    filteredGames.value = []
    filteredSections.value = []

    ElMessage.success('表单已重置')
  })
}

// 初始化
const init = async () => {
  // 检查登录状态
  const authData = getAuthData()
  if (!authData) return

  try {
    // 加载所有数据
    await Promise.all([
      loadGameTypes(),
      loadGames(),
      loadSections()
    ])

    // 延迟初始化编辑器，确保DOM已渲染
    setTimeout(() => {
      initEditor()
    }, 100)

    // 检查路由参数并自动填充
    const sectionId = route.query.sectionId
    if (sectionId) {
      // 延迟执行自动填充，确保数据已加载完成
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

// 组件挂载
onMounted(() => {
  init()
})

// 组件卸载前清理
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

.upload-section {
  width: 100%;
}

.upload-area {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  transition: border-color 0.3s;
  overflow: hidden;
}

.upload-area:hover {
  border-color: #409eff;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  background: #fafafa;
  transition: all 0.3s;
}

.upload-area:hover .upload-placeholder {
  background: #f5f7fa;
}

.upload-icon {
  font-size: 28px;
  color: #c0c4cc;
  margin-bottom: 16px;
}

.upload-text {
  color: #606266;
  font-size: 14px;
  margin-bottom: 8px;
}

.upload-tip {
  color: #909399;
  font-size: 12px;
}

.image-preview {
  position: relative;
  display: inline-block;
  width: 200px;
  height: 150px;
  margin: 10px;
  border-radius: 6px;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.editor {
  height: 300px;
}

.content-tip {
  padding: 8px 12px;
  background: #f5f7fa;
  color: #909399;
  font-size: 12px;
  border-top: 1px solid #ebeef5;
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
  min-height: 250px;
  padding: 12px 15px;
}

:deep(.ql-editor.ql-blank::before) {
  color: #c0c4cc;
  font-style: normal;
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

  .image-preview {
    width: 150px;
    height: 120px;
  }
}
</style>