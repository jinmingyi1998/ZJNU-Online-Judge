$(function () {
    $(".change-problem").click(function () {
        $(".container#problem-container").show();
        var problem = Object();
        cid = $("main").attr("id");
        pid = $(this).attr("id");
        $.get({
            url: "/contest/rest/" + cid + "/problem/" + pid,
            success: function (data) {
                problem = data;
                $("#problem-title").text(problem.title);
                $("#problem-time-limit").text(problem.timeLimit);
                $("#problem-memory-limit").text(problem.memoryLimit);
                $("#problem-description").text(problem.description);
                $("#problem-input").text(problem.input);
                $("#problem-output").text(problem.output);
                $("#problem-sample-input").text(problem.sampleInput);
                $("#problem-sample-output").text(problem.sampleOutput);
                $("#problem-hint").text(problem.hint);
                $("#submit_btn").attr("problem-id", pid);
            }
        });
    });
});

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
                    try {
                        $("#modal-ce").text(solution.ce.info);
                    } catch (e) {
                    }
                    $("#modal-username").text(solution.user.username);
                    $("#modal-problem").text(solution.problem['id']);
                    $("#modal-result").text(solution.result);
                    if (solution.result == "Accepted") {
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
            url: "/status/share//" + $("#modal-id").text(),
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

function getStatusOfMe() {
    cid = $("main").attr("id");
    $.get({
        url: "/contest/rest/status/" + cid,
        success: function (data) {
            data.forEach(function (e) {
                var tem = "<tr class='status_row'>" +
                    "<td>-</td>" +
                    "<td>-</td>" +
                    "<td>-</td>" +
                    "<td class='result view-code'>-</td>" +
                    "<td>-</td>" +
                    "<td>-</td>" +
                    "<td>-</td>" +
                    "<td class='view-code'>-</td>" +
                    "<td>-</td>" +
                    "</tr>";
                tem = tem.split("-");
                if (e.language == "py2") e.language = "Python2";
                if (e.language == "py3") e.language = "Python3";
                if (e.language == "cpp") e.language = "C++";
                e.language = e.language.toUpperCase();
                $("#status-tbody").append(tem[0] + e.id + tem[1] + e.user.username + tem[2] +
                    e.problem.id + tem[3] + e.result + tem[4] + e.time + tem[5] + e.memory +
                    tem[6] + e.length + tem[7] + e.language + tem[8] + e.submitTime + tem[9]);
                if (e.result == "Accepted") {
                    $(".status_row").last().children(".result").addClass("text-success font-weight-bold");
                } else {
                    $(".status_row").last().children(".result").addClass("text-danger");
                }
            });
        }
    });
}