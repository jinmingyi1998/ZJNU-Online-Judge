/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

var cid;
$(function () {
    cid = $("main").attr("id");
    changeProblem($("main").attr("id"), $(".change-problem").first().attr("id"));
    $(".change-problem").first().addClass("active");
    $(".change-problem").click(function () {
        changeProblem(cid, $(this).attr("id"))
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
            ve._data.status = data;
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
                rankbody.empty();
                peo.forEach(function (e) {
                    var html_str = "";
                    var plist = [];
                    html_str += "<tr><td>" + e.user.name + "</td>";
                    html_str += "<td>" + e.penalty + "</td>";
                    html_str += "<td>" + e.ac + "</td>";
                    e.problems.forEach(function (pp) {
                        plist[pp.pid] = pp;
                    });
                    for (var i = 1; i <= psize; i++) {
                        if (typeof (plist[i]) == "undefined") {
                            html_str += "<td></td>";
                            continue;
                        }
                        html_str += "<td ";
                        var str = " ";
                        if (plist[i].firstblood == true)
                            str += "class='bg-success'";
                        if (plist[i].ac == true) {
                            str += ">" + plist[i].duration + "(" + (parseInt(plist[i].wa) + 1) + ")";
                        } else {
                            str += "class='bg-danger'>(" + plist[i].wa + ")";
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
    console.log(1);
    $("#problem-container").show();
    var problem = Object();
    $.get({
        url: "/contest/rest/" + cid + "/problem/" + pid,
        success: function (problem) {
            $("#problem-title").text(problem.title);
            $("#problem-time-limit").text(problem.timeLimit);
            $("#problem-memory-limit").text(problem.memoryLimit);
            $("#problem-description").html(markdown.makeHtml(problem.description));
            $("#problem-input").html(markdown.makeHtml(problem.input));
            $("#problem-output").html(markdown.makeHtml(problem.output));
            $("#problem-sample-input").text(problem.sampleInput);
            $("#problem-sample-output").text(problem.sampleOutput);
            $("#problem-hint").html(markdown.makeHtml(problem.hint));
            $("#submit_btn").attr("problem-id", pid);
            try {
                MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
            } catch (e) {
                ;
            }
        }
    });
}

function getComments() {
    $.get({
        url: "/contest/rest/" + cid + "/comments",
        success: function (res) {
            ce._data.comments = res;
        }
    });
}