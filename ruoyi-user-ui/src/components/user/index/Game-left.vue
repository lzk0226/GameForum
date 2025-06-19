<template>
  <div class="game-recommendation">
    <h3 @click="goToGamePage">游戏推荐</h3>
    <div v-if="loading" class="loading">
      加载中...
    </div>
    <div v-else-if="error" class="error">
      加载失败
    </div>
    <div v-else class="game-types-container">
      <div v-for="gameType in gameTypes" :key="gameType.typeId" class="game-type-section">
        <div class="game-type-title" @click="toggleGameType(gameType.typeId)">
          <span class="type-name">{{ gameType.typeName }}</span>
          <span class="toggle-icon" :class="{ 'expanded': expandedTypes.includes(gameType.typeId) }">▼</span>
        </div>
        <div v-show="expandedTypes.includes(gameType.typeId)" class="games-list">
          <div v-if="gameType.games && gameType.games.length > 0">
            <div v-for="game in gameType.games" :key="game.gameId" class="game-item" @click="selectGame(game)">
              <div class="game-name">{{ game.gameName }}</div>
              <div class="game-brief">{{ truncateText(game.gameDescription, 30) }}</div>
              <div class="game-tag">{{ game.remark || '推荐游戏' }}</div>
            </div>
          </div>
          <div v-else class="no-games">
            暂无游戏
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GameRecommendation',
  data() {
    return {
      gameTypes: [],
      loading: true,
      error: null,
      expandedTypes: [] // 展开的游戏类型ID列表
    }
  },
  async mounted() {
    await this.loadGameData()
  },
  methods: {
    async loadGameData() {
      try {
        this.loading = true
        this.error = null

        // 获取所有游戏数据
        const gameResponse = await fetch('http://localhost:8080/user/game/list')
        if (!gameResponse.ok) {
          throw new Error('获取游戏数据失败')
        }
        const gameResult = await gameResponse.json()

        if (gameResult.code !== 200) {
          throw new Error(gameResult.msg || '获取游戏数据失败')
        }

        // 获取所有游戏类型数据
        const typeResponse = await fetch('http://localhost:8080/user/gameType/all')
        if (!typeResponse.ok) {
          throw new Error('获取游戏类型数据失败')
        }
        const typeResult = await typeResponse.json()

        if (typeResult.code !== 200) {
          throw new Error(typeResult.msg || '获取游戏类型数据失败')
        }

        // 按游戏类型分组游戏数据
        this.gameTypes = this.groupGamesByType(gameResult.data, typeResult.data)

        // 默认展开所有游戏类型
        this.expandedTypes = this.gameTypes.map(type => type.typeId)

      } catch (error) {
        console.error('加载游戏数据失败:', error)
        this.error = error.message || '加载数据失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },

    groupGamesByType(games, gameTypes) {
      // 创建游戏类型映射
      const typeMap = {}
      gameTypes.forEach(type => {
        typeMap[type.typeId] = {
          typeId: type.typeId,
          typeName: type.typeName,
          games: []
        }
      })

      // 将游戏按类型分组，每个类型最多3款游戏
      games.forEach(game => {
        if (typeMap[game.gameTypeId] && typeMap[game.gameTypeId].games.length < 3) {
          typeMap[game.gameTypeId].games.push(game)
        }
      })

      // 返回有游戏的类型
      return Object.values(typeMap).filter(type => type.games.length > 0)
    },

    toggleGameType(typeId) {
      const index = this.expandedTypes.indexOf(typeId)
      if (index > -1) {
        this.expandedTypes.splice(index, 1)
      } else {
        this.expandedTypes.push(typeId)
      }
    },

    // 点击标题跳转到游戏页面
    goToGamePage() {
      this.$router.push({name: 'GamePage'})
    },

    // 点击具体游戏跳转到游戏详情页
    selectGame(game) {
      // 发出事件（保留原有功能）
      this.$emit('game-selected', game)

      // 跳转到游戏详情页，可以传递游戏ID作为参数
      this.$router.push({
        name: 'Game',
        query: {gameId: game.gameId} // 通过query参数传递游戏ID
      })

      console.log('Selected game:', game.gameName)
    },

    truncateText(text, maxLength) {
      if (!text) return ''
      return text.length > maxLength ? text.slice(0, maxLength) + '...' : text
    }
  }
}
</script>

<style scoped>
.game-recommendation {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background-color: #fff;
  box-sizing: border-box;
  height: fit-content;
}

.game-recommendation h3 {
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  text-align: center;
  padding-bottom: 8px;
  border-bottom: 2px solid #007bff;
  cursor: pointer; /* 添加鼠标指针样式 */
  transition: color 0.2s ease; /* 添加过渡效果 */
}

.game-recommendation h3:hover {
  color: #007bff; /* 鼠标悬停时改变颜色 */
}

.loading, .error {
  text-align: center;
  padding: 15px 8px;
  font-size: 12px;
  color: #666;
}

.error {
  color: #d32f2f;
}

.game-types-container {
  margin-top: 8px;
}

.game-type-section {
  margin-bottom: 12px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  overflow: hidden;
}

.game-type-title {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 8px 10px;
  font-size: 13px;
  font-weight: 600;
  color: #495057;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.2s ease;
  border-bottom: 1px solid #dee2e6;
}

.game-type-title:hover {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1976d2;
}

.type-name {
  flex: 1;
  font-size: 12px;
}

.toggle-icon {
  transition: transform 0.2s;
  font-size: 10px;
  color: #6c757d;
}

.toggle-icon.expanded {
  transform: rotate(180deg);
}

.games-list {
  padding: 6px;
}

.game-item {
  padding: 8px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.2s ease;
  border-radius: 3px;
  margin-bottom: 4px;
}

.game-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.game-item:hover {
  background-color: #f8f9fa;
  transform: translateX(2px);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.game-name {
  font-size: 13px;
  font-weight: 600;
  color: #007bff;
  margin-bottom: 3px;
  line-height: 1.2;
}

.game-brief {
  font-size: 11px;
  color: #666;
  line-height: 1.3;
  margin-bottom: 4px;
}

.game-tag {
  font-size: 10px;
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 1px 6px;
  border-radius: 8px;
  display: inline-block;
  max-width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.no-games {
  text-align: center;
  color: #999;
  padding: 12px 8px;
  font-size: 11px;
  font-style: italic;
}
</style>