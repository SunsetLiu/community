function like(btn, entityType, entityId, entityUserId, discussPostId) {

    $.post(
        CONTEXT_PATH + "/like",
        {"entityType":entityType, "entityId":entityId, "entityUserId":entityUserId, "discussPostId":discussPostId},
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0){
                $(btn).children("i").text(data.entityLikeCount);
                $(btn).children("b").text(data.userLikeStatus==1?"已赞":"赞");
            }else {
                alert(data.msg);
            }
        }
    );

}