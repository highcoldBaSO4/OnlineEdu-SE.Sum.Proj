export default {
    requestHeader: function () {
        return {
            Authorization: "Bearer " + localStorage.getItem("managerToken")
        }
    }
}
