<template>
  <div id="app">
    <router-view />
    <theme-picker />
    <audio controls ref="audio" muted="muted" :src="audioSrc" style="display:none;"></audio>
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";

export default {
  name: "App",
  components: { ThemePicker },
  data(){
    return {
      audioSrc:require('@/assets/audio/notice.mp3')
    }
  },
    metaInfo() {
        return {
            title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
            titleTemplate: title => {
                return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
            }
        }
    },
  created() {

    // speechSynthesis.speak(new SpeechSynthesisUtterance("1号车已结束"))
    // const synth = window.speechSynthesis; // 启用文本
    // const msg = new SpeechSynthesisUtterance(); // 表示一次发音请求。其中包含了将由语音服务朗读的内容，以及如何朗读它（例如：语种、音高、音量）。
    // console.log(synth)
    // console.log(msg)
    // msg.text = "1号车已结束"; // 文字内容: 测试内容
    // msg.lang = "zh-cn"; // 使用的语言:中文
    // msg.volume = 1; // 声音音量：1
    // msg.rate = 1; // 语速：1
    // msg.pitch = 1; // 音高：1
    // synth.speak(msg); // 播放
    // 因为我的页面有缓存机制，用户下次有可能直接打开某个登录后才能访问的页面 比如F5刷新了某个页面 需要重连
    // 又比如后端服务器因为什么原因突然中断了一下 也需要重新连接WebSocket
    // 每3秒检测一次websocket连接状态 未连接 则尝试连接 尽量保证网站启动的时候 WebSocket都能正常长连接
    setInterval(this.WebSocket_StatusCheck, 3000);
    this.$WebSocket.WebSocketBandMsgReceivedEvent(this.WebSocket_OnMesage) // 绑定消息回调事件
  },
  methods: {
    //接收消息
    WebSocket_OnMesage(msg) {//实际消息回调事件
      this.handleSpeak(msg.data)
    },
    // 语音播报的函数
    handleSpeak() {
      console.log(this.$refs.audio)
      this.$refs.audio.play();
    },
    // 1、WebSocket连接状态检测：
    WebSocket_StatusCheck() {
      if (!this.$WebSocket.WebSocketHandle || this.$WebSocket.WebSocketHandle.readyState !== 1) {
        console.log('Websocket连接中断，尝试重新连接:')
        this.WebSocketINI()
      }
    },

    // 2、WebSocket初始化：
    async WebSocketINI() {
      // 1、浏览器是否支持WebSocket检测
      if (!('WebSocket' in window)) {
        console.log('您的浏览器不支持WebSocket!')
        return
      }

      // 2、从后台提取WebScoket服务器连接地址：根据自己业务接口获取 或者直接跳过 下面直接写死
      const tmpWebsocketSrverAddress = process.env.VUE_APP_WEBSOCKET_PATH + this.$store.getters.token //可以直接赋值如：ws://127.0.0.1:1234

      // 3、创建Websocket连接
      const tmpWebsocket = new WebSocket(tmpWebsocketSrverAddress)

      // 4、全局保存WebSocket操作句柄：main.js 全局引用
      this.$WebSocket.WebsocketINI(tmpWebsocket)

      // 5、WebSocket连接成功提示
      tmpWebsocket.onopen = function(e) {
        console.log('webcoket连接成功')
      }

      //6、连接失败提示
      tmpWebsocket.onclose = function(e) {
        console.log('webcoket连接关闭：', e)
      }
    }

  }
};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
