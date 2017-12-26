<title>系统故障</title>
<body>
<div class="right-all">
    <div class="info">
        <div class="error-page">
            <h3 class="error-reason">
                非常抱歉，系统出错了
                <br/>
                请简单描述一下您刚才的操作，帮助我们改进系统
                <br/>

                <form id="feedbackForm" action='${feedbackUrl!"/"}' method="post" target="abc">
                    <textarea style="width:580px;height:150px" name="bugDesc" id="bugDesc"></textarea>
                    <br/>
                    <input type="hidden" name="uuid" id="uuid" value="${uuid!""}" />
                    <input type="submit" value="提交报告" id="btnFeedback" />
                </form>
                <iframe name="abc" style="display: none"></iframe>
                <br/>
            </h3>
            <div class="error-sys"></div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $("#btnFeedback").click(function () {
            var bugDesc = $("#bugDesc").val();
            if(bugDesc == null || bugDesc == '') {
                alert('请简单描述一下您刚才的操作，帮助我们改进系统');
                return;
            }
            setTimeout('location.href="/";', 1000);
        })

    });
</script>
</body>
