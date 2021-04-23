<template>
    <div>
        <el-page-header :content="videoTitle" @back="lastPage"></el-page-header>
        <video ref="resourceVideo" class="video-js video-player" @firstplay="alert('play')"></video>
    </div>
</template>

<script>
    import Video from "video.js";
    import "video.js/dist/video-js.css";
    import zhCN from "video.js/dist/lang/zh-CN.json";

    export default {
        name: "UserCourseVideo",
        props: {
            videoUrl: {
                default: ""
            },
            videoTitle: {
                default: ""
            }
        },
        data() {
            return {
                videoOptions: {
                    autoplay: false,
                    preload: "auto",
                    //src: this.fullVideoUrl,
                    width: "900px",
                    height: "600px",
                    language: 'zh-CN',
                    sources: [
                        {
                            src: this.fullVideoUrl(),
                            type: this.videoType()
                        }
                    ],
                    controls: true,
                    controlBar: {
                        currentTimeDisplay: true,
                        volumePanel: {
                            inline: false
                        },
                        playbackRateMenuButton: {
                            playbackRates: [0.5, 0.75, 1, 1.25, 1.5, 1.75, 2],
                        }
                    }
                },
                videoPlayer: null,
            }
        },
        methods: {
            fullVideoUrl: function () {
                return "http://202.120.40.8:30382/online-edu/static/" + this.videoUrl;
            },
            videoType: function () {
                return "video/" + this.videoUrl.split(".").pop();
            },
            lastPage: function () {
                this.$router.back();
            },
        },
        computed: {

        },
        mounted() {
            zhCN["Picture-in-Picture"] = "悬浮框播放";
            Video.addLanguage("zh-CN", zhCN);
            this.videoPlayer = Video(this.$refs.resourceVideo, this.videoOptions, function onPlayerReady () {

            });
            this.videoPlayer.on("firstplay", () => {
                this.$root.recordStartPlay(this.videoTitle, this.videoPlayer.currentTime());
            });
            this.videoPlayer.on("pause", () => {
                this.$root.recordPause(this.videoTitle, this.videoPlayer.currentTime());
            });
            this.videoPlayer.on("play", () => {
                this.$root.recordContinue(this.videoTitle, this.videoPlayer.currentTime());
            });
            this.videoPlayer.on("ratechange", () => {
                this.$root.recordChangeSpeed(this.videoTitle, this.videoPlayer.currentTime(), this.videoPlayer.playbackRate());
            });
            // this.videoPlayer.on("seeking", () => {
            //     console.log(this.videoPlayer.currentTime());
            // });
            this.videoPlayer.on("seeked", () => {
                this.$root.recordJump(this.videoTitle, this.videoPlayer.currentTime());
                //console.log(this.videoPlayer.currentTime());
            })
        },
        beforeDestroy() {
            this.$root.recordFinishPlay(this.videoTitle, this.videoPlayer.currentTime());
            if (this.videoPlayer) {
                this.videoPlayer.dispose();
            }
        }
    }
</script>

<style scoped>
    .video-player {
        margin-top: 30px;
        margin-left: 20px;
    }
</style>
