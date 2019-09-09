<template>
    <div>
        <AddNewChapter
                slot="title"
                :last-chapter="'0'"
                v-if="isCourseTeacher"
        ></AddNewChapter>
        <el-collapse>
            <el-collapse-item
                    v-for="(chapter, indexChapter) in chapters"
                    :key="indexChapter"
            >
                <h2 slot="title">{{ chapter.title }}&nbsp;&nbsp;&nbsp;&nbsp;</h2>
<!--                <h3>{{chapter.secNo}}</h3>-->
                <AddNewChapter slot="title" :last-chapter="chapter.secNo" v-if="isCourseTeacher"></AddNewChapter>
                <div class="section-content">
                    <el-collapse>
                        <AddNewSection :chapterId="chapter.sectionPrimaryKey.secId"
                                       :last-section="'0'"
                                       v-if="isCourseTeacher"
                        ></AddNewSection>
                        <CourseSectionUnit
                                v-for="(section, indexSection) in chapter.sectionBranchesList"
                                :key="indexSection"
                                :section-info="section"
                                :chapterId="chapter.sectionPrimaryKey.secId"
                        ></CourseSectionUnit>
                    </el-collapse>
                </div>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script>
    import CourseSectionUnit from "./CourseSectionUnit";
    import AddNewChapter from "./AddNewChapter";
    import AddNewSection from "./AddNewSection";
    import { mapGetters } from "vuex";

    export default {
        name: "CourseChapters",
        components: {AddNewChapter, CourseSectionUnit, AddNewSection},
        data() {
            return {
                //chapters: this.$store.getters.getCourseInfo.sectionList,

                showAddChapter: false,
                addAfterChapter: {
                    id: 0,
                    title: "",
                }
            }
        },
        computed: {
            ...mapGetters([
                'isCourseTeacher',
            ]),
            chapters: function () {
                var section=this.$store.getters.getCourseInfo.sectionList;
                section.sort((a,b) => {
                    if (a.secNo === b.secNo) {
                        if (a.path < b.path) return -1;
                        else return 1;
                    }
                    else {
                        if (a.secNo < b.secNo) return -1;
                        else return 1;
                    }
                });
                return section;
            }
        }
    }
</script>

<style scoped>
    .section-content {
        padding-left: 20px;
        padding-right: 20px;
    }
</style>
