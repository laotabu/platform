<template>

  <iframe :src="fullUrl" width="100%" height="800px" />

</template>

<script>
export default {
  name: 'PdfFrame',
  props: {
    src: String
  },
  data() {
    return {
      // viewUrl: 'D:/ruoyi/uploadPath',
      viewUrl: '/pdf/web/viewer.html?file=',
      //viewUrl: 'http://192.168.2.88:9000',
      fullUrl: ''
    }
  },
  watch: {

    src: {
      handler() {
        this.fullUrl = this.viewUrl + this.src
        console.log(this.fullUrl)
      }
    }
  },
  created() {
    this.fullUrl = this.viewUrl + this.src
  }

}

</script>
