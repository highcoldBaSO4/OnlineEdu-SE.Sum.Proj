<template>
    <span>{{ dateRange }}</span>
</template>

<script>
    export default {
        name: "DateRangeFormat",
        props: {
            start: {
                default: "",
            },
            end: {
                default: "",
            },
            showMinute: {
                default: false
            },
            originText: {
                default: true
            }
        },
        computed: {
            dateRange: function () {
                if (this.showMinute) {
                    return this.start.substr(0,10) + " " + this.start.substr(11,8)
                        + " ~ "
                        + this.end.substr(0,10) + " " + this.end.substr(11,8);
                }
                else {
                    return this.start.substr(0,10) + " ~ " + this.end.substr(0,10)
                }
            }
        }
    }
</script>

<style scoped>

</style>
