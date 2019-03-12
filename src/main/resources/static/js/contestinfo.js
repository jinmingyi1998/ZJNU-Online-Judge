var cid;
$(function () {
    cid = $("main").attr("id");
    changeProblem($("main").attr("id"), $(".change-problem").first().attr("id"));
    $(".change-problem").first().addClass("active");
    $(".change-problem").click(function () {
        changeProblem($("main").attr("id"), $(this).attr("id"))
    });
    $(function () {
        $("body").on('click', '.view-code', function () {
            var id = $(this).attr("id");
            $.post(
                {
                    url: "/contest/status/view/" + id,
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
                            $("#modal-result").attr("class", "text-success font-weight-bold");
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
});
function getStatusOfMe() {
    cid = $("main").attr("id");
    $.get({
        url: "/contest/rest/status/" + cid,
        success: function (data) {
            var users = Array();
            var problems = Array();
            $("#status-tbody").empty();
            data.forEach(function (e) {
                if (typeof (e.user.id) == "undefined") {
                    e.user = users[e.user];
                } else {
                    users[e.user.id] = e.user;
                }
                if (typeof (e.problem.id) == "undefined") {
                    e.problem = problems[e.problem];
                } else {
                    problems[e.problem.id] = e.problem;
                }

                var tem = "<tr class='status_row'>" +
                    "<td>*</td>" +
                    "<td>*</td>" +
                    "<td>*</td>" +
                    "<td class='result view-code'>*</td>" +
                    "<td>*</td>" +
                    "<td>*</td>" +
                    "<td>*</td>" +
                    "<td class='view-code'>*</td>" +
                    "<td>*</td>" +
                    "</tr>";
                tem = tem.split("*");
                if (e.language == "py2") e.language = "Python2";
                if (e.language == "py3") e.language = "Python3";
                if (e.language == "cpp") e.language = "C++";
                $("#status-tbody").append(tem[0] + e.id + tem[1] + e.user.username + tem[2] +
                    e.problem.id + tem[3] + e.result + tem[4] + e.time + tem[5] + e.memory +
                    tem[6] + e.length + tem[7] + e.language + tem[8] + e.submitTime + tem[9]);
                if (e.result == "Accepted") {
                    $(".status_row").last().children(".result").addClass("text-success font-weight-bold");
                } else {
                    $(".status_row").last().children(".result").addClass("text-danger");
                }
                $(".status_row").last().children(".view-code").each(function () {
                        $(this).attr("id", e.id)
                    }
                );
            });
        }
    });
}

function getRankOfContest() {
    $.get(
        {
            url: "/contest/rest/rank/" + cid,
            success: function (rank) {
                peo = rank.people;
                psize = $("#problem-number").text();
                rankbody = $("#rank-tbody");
                peo.forEach(function (e) {
                    var html_str = "";
                    var plist = new Array();
                    html_str += "<tr><td>" + e.user.name + "</td>";
                    html_str += "<td>" + e.penalty + "</td>";
                    html_str += "<td>" + e.ac + "</td>";
                    e.problems.forEach(function (pp) {
                        plist[pp.pid] = pp;
                    });
                    for (var i = 1; i <= psize; i++) {
                        if (typeof (plist) == "undefined") {
                            html_str += "<td></td>";
                            continue;
                        }
                        html_str += "<td ";
                        var str = " ";
                        if (plist[i].firstblood == true)
                            str += "class='bg-success'";
                        if (plist[i].ac == true) {
                            str += plist[i].duration + ">(" + (parseInt(plist[i].wa) + 1) + ")";
                        } else {
                            str += "class='bg-danger'>(" + plist.wa + ")";
                        }
                        html_str += str + "</td>";
                    }
                    html_str += "</tr>";
                    rankbody.append(html_str);
                });
            }
        }
    );
}

function changeProblem(cid, pid) {
    $("#problem-container").show();
    var problem = Object();
    $.get({
        url: "/contest/rest/" + cid + "/problem/" + pid,
        success: function (problem) {
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
}