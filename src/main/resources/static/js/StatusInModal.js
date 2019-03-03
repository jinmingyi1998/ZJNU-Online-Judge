$(function () {
    $(".view-code").click(function () {
        var id = $(this).attr("id");
        $.post(
            {
                url: "/status/view/" + id,
                success: function (data) {
                    var solution = data;
                    $(".prettyprint").attr("class", "prettyprint");
                    $("#modal-id").text(solution.id);
                    $("#modal-ce").text(solution.ce);
                    $("#modal-username").text(solution.username);
                    $("#modal-problem").text(solution.problemId);
                    $("#modal-result").text(solution.result);
                    if (solution.result == "Accepted") {
                        $("#modal-result").attr("class", "text-success font weight-bold");
                    }
                    else {
                        $("#modal-result").attr("class", "text-danger");
                    }
                    $("#modal-language").text(solution.normalLanguage);
                    $("#modal-submit-time").text(solution.normalSubmitTime);
                    $("#modal-memory").text(solution.memory);
                    $("#modal-length").text(solution.length);
                    $("#modal-time").text(solution.time);
                    $("#source_code").text(solution.source);
                    PR.prettyPrint();
                    $("#codeModal").modal('show');
                    if (solution.share) {
                        $("#modal-share").text("Sharing");
                        $("#modal-share").attr("class", "btn btn-sm btn-success");
                    }
                    else {
                        $("#modal-share").text("Not Shared");
                        $("#modal-share").attr("class", "btn btn-sm btn-danger");
                    }
                }
            }
        );
    });
    $("#modal-share").click(function () {
        $.post({
            url: "/status/share/" + $("#modal-id").text(),
            success: function (data) {
                if (data == true) {
                    $("#modal-share").text("Sharing");
                    $("#modal-share").attr("class", "btn btn-sm btn-success");
                }
                else {
                    $("#modal-share").text("Not Shared");
                    $("#modal-share").attr("class", "btn btn-sm btn-danger");
                }
            }
        });
    });
});