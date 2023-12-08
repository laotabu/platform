<template>
  <iframe :src="fullUrl" width="100%" height="800px" />
</template>

<script>
export default {
  name: 'weeklyFrame',
  data() {
    return {
      viewUrl: '/old/view/weekly.html',
      fullUrl: ''
    }
  },
  watch: {

    src: {
      handler() {
        this.fullUrl = this.viewUrl
      }
    }
  },
  created() {
    this.fullUrl = this.viewUrl
  }

}

</script>
