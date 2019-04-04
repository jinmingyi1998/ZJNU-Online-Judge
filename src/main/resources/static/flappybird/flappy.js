window.onload = function () {
    function $(idName) {
        return document.getElementById(idName);
    }

    var gameStart = $("gameStart");
    var gameEnter = $("gameEnter");
    var myBird = $("myBird"), birdImg = myBird.firstElementChild;
    var btn = $("btn");
    var pipes = $("pipes");
    var status = false, flyHeight = 20, a = null, score = 0;    // 初始化游戏状态

    var allPipes = [];

//  console.log(birdImg.src.match(/\/([a-zA-Z]+)\./)[1]);
    btn.onclick = function () {
        gameStart.style.display = "none";
        gameEnter.style.display = "block";
        // 按空格实现游戏的开始与暂停
        document.onkeyup = function (event) {
            var evt = event || window.event;
            var char = evt.keyCode;
            if (char == 32) {
                if (!status) {
                    // 开始游戏
                    birdFly();
                    gameEnter.onclick = function() {
                        birdFly(true);
                    };
                    create();
                    continueGame();
                } else {
                    // 暂停游戏
                    pause();
                }
            }
            status = !status;
        }
    };

    function birdFly(flag) {
        if (myBird.timer) {
            clearInterval(myBird.timer);
            delete myBird.timer;
        }
        var speed = flag ? -1 : 1.5;
        var flyHeight = 20;
        var end = getStyle(myBird,"top") - flyHeight;
        myBird.timer = setInterval(function () {
            var birdImg = myBird.firstElementChild;
            var moveVal = getStyle(myBird, "top");
            if(moveVal <= 0 ) speed = 1.5;
            if (moveVal == end) {
                speed = 1.5;
                myBird.style.top = moveVal + speed + "px";
            } else {
                myBird.style.top = Math.min(480-26-42-14, moveVal+speed)+"px";
                var imgName = birdImg.src.match(/\/([a-zA-Z]+)\./)[1];
                if (speed > 0 && imgName != "down") {
                    birdImg.src = "/flappybird/img/down.gif";
                }else if (speed <0 && imgName != "up") {
                    birdImg.src = "/flappybird/img/up.gif";
                }
                if (moveVal >= 480-26-42-14) {
                    clearInterval(myBird.timer);
                    delete myBird.timer;
                }
            }
        }, 10);
    }

    function create() {
        a = setInterval(function () {
            createPipe();
        }, 2000)
    }

    function createPipe() {
        var pipes = document.getElementById("pipes");
        var distance = 100;
        var div = document.createElement("div");
        div.className = "p";
        div.s = true;
        var rndUpModH = rndHeight();
        var rndLowModH = 480 - 42 - 14 - distance - rndUpModH - 60 - 60;
        div.innerHTML = `<div class="upper"><div class="up_mod" style="height: ${rndUpModH}px"></div><div class="up_pipe"></div></div><div class="lower"><div class="low_pipe"></div><div class="low_mod" style="height: ${rndLowModH}px"></div></div>`;
        pipes.appendChild(div);
        pipeMove(div);
        allPipes.push(div);
    }

    function rndHeight() {
        return Math.floor(Math.random()*(480 - 42 - 14 - 60 - 60 - 100 + 1));
    }

    function pipeMove(p) {
        var speed = -1;
        p.timer = setInterval(function () {
            var moveVal = getStyle(p,"left");
            if (moveVal < -62) {
                clearInterval(p.timer);
                p.timer = undefined;
                pipes.removeChild(p);
                allPipes.splice(0, 1);
            } else {
                p.style.left = moveVal + speed + "px";
                if (moveVal + speed <= 60 - 62 && p.s) {
                    p.s = false;
                    scores.innerHTML = ++score;
                }
            }
            impact();
        }, 10)
    }

    function impact() {
        var myBirdT = getStyle(myBird, "top");
        for (var i=0; i < allPipes.length; i++) {
            if (allPipes[i].s) {
                var distance = 100;
                var pLeft = getStyle(allPipes[i],"left");
                var upperH = getStyle(allPipes[i].firstElementChild, "height");
                if ((60 + 40) >= pLeft && 60 <= pLeft + 62 &&( myBirdT <= upperH || myBirdT + 26 >= upperH + distance )) {
                    alert("Game Over: " + score + " Point(s)");
                    gameOver();
                }
            }
        }
    }

    function gameOver() {
        clearInterval(a);
        clearInterval(myBird.timer);
        gameEnter.style.display = "none";
        gameStart.style.display = "block";
        gameEnter.onclick = null;
        score = 0;
        scores.innerHTML = 0;
        s = false;
        for (var i=allPipes.length - 1;i >= 0;i--) {
            clearInterval(allPipes[i].timer);
            pipes.removeChild(allPipes[i]);
        }
        allPipes = [];
    }

    function pause() {
        clearInterval(a);
        clearInterval(myBird.timer);
        gameEnter.onclick = null;
        for (var i = 0; i < allPipes.length; i++) {
            clearInterval(allPipes[i].timer);
        }
    }

    function continueGame() {
        for (var i =0; i < allPipes.length; i++) {
            pipeMove(allPipes[i]);
        }
    }
    
    function getStyle(elem, attr) {
        var res = null;
        if (elem.currentStyle) {
            res = elem.currentStyle[attr];
        } else {
            res = window.getComputedStyle(elem, null)[attr];
        }
        return parseFloat(res);
    }
};