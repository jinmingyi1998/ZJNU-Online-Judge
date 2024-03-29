/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

$(function () {
    $("body").on("click",".view-code",function () {
        var id = $(this).attr("id");
        $.post(
            {
                url: "/status/view/" + id,
                success: function (data) {
                    var solution = data;
                    $(".prettyprint").attr("class", "prettyprint");
                    $("#modal-id").text(solution.id);
                    try {
                        $("#modal-ce").text(solution.ce.info);
                    } catch (e) {
                    }
                    $("#modal-username").text(solution.user.username);
                    $("#modal-problem").text(solution.problem['id']);
                    $("#modal-result").text(solution.normalResult);
                    if (solution.normalResult == "Accepted") {
                        $("#modal-result").attr("class", "text-success font weight-bold");
                    } else {
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
                    } else {
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
                } else {
                    $("#modal-share").text("Not Shared");
                    $("#modal-share").attr("class", "btn btn-sm btn-danger");
                }
            }
        });
    });
});