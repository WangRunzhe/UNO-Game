/* global Handlebars */
$(function () {
    var socket = null;
    var myParam = location.search.split('playerid=')[1];
    $("#waiting").hide();
    $("#ondeck").hide();
    $("#onhand").hide();
    $("#drawcard").hide();
    $("#createBtn").on("click", function () {
        socket = new WebSocket("ws://localhost:8080/playcard/game/" + $("#gameid").val());
        var refreshHandcard = {
            cmd: "refresh-handcard",
            gameid: $("#gameid").val(),
            playerid: myParam

        };
        socket.onopen = function () {
            console.log("Conneccted");
            var gameinfo = {
                cmd: "new-game",
                description: $("#destxt").val(),
                gameid: $("#gameid").val(),
                playernumber: $("#playernum").val(),
                playerid: myParam
            };

            socket.send(JSON.stringify(gameinfo));
        };

        socket.onmessage = function (evt) {
            //update---1.26
            // 从href的url中拿到playerid
            var myParam = location.search.split('playerid=')[1];
            console.log(myParam);


            var msgback = JSON.parse(evt.data);
            if (msgback.status === "successfully") {
                $("#creategame").hide();
                $("#waiting").show();
                $("#showgameinfo").text("Game Id: " + msgback.gameid);


                //update---1.26
                $("#showgameid").text("Creater Id: " + myParam);
                $("#showplayernum").text("Player Num: " + msgback.playernumber);
                console.log("in waiting div");

            }

            switch (msgback.cmd) {
                case "start-game":
                    $("#waiting").hide();
                    $("#ondeck").show();
                    $("#onhand").show();
                    $("#drawcard").show();
                    console.log(msgback);
                    var HandcardArray = new Array();
                    for (var i = 0; i < msgback.card.length; i++)
                        HandcardArray[i] = msgback.card[i].cardID;
                    $(function () {
                        var cardtemplate = Handlebars.compile($("#card-template").html());
                        for (var i = 0; i < HandcardArray.length; i++) {
                            console.log(HandcardArray[i]);
                            $("#cards").append(cardtemplate({cid: HandcardArray[i]}));
                        }
                    });
                    break;
                case "send-card":
                    var card = msgback.card.cardID;
                    $("#showcard").text(card);
                    $("#tablecard").attr("src","uno_deck/"+card+".png");
                    console.log(myParam);
                    if (myParam == msgback.playerid)
                        socket.send(JSON.stringify(refreshHandcard));
                    break;
                case "refresh-handcard":

                    var HandcardArray = new Array();
                    for (var i = 0; i < msgback.card.length; i++)
                        HandcardArray[i] = msgback.card[i].cardID;
                    $("#nowcard").text(HandcardArray);
                    break;
                case "draw-card":
                    var HandcardArray = new Array();
                    for (var i = 0; i < msgback.card.length; i++)
                        HandcardArray[i] = msgback.card[i].cardID;
                    $(function () {
                        var cardtemplate = Handlebars.compile($("#card-template").html());

                        $("#cards").append(cardtemplate({cid: HandcardArray[msgback.card.length - 1]}));
                    });
                    break;
                default :
                    break;
            }
            ;

        };
    });

    $("#startgameBtn").on("click", function () {

        var msg = {
            cmd: "start-game",
            gameid: $("#gameid").val(),
            playerid: myParam
        };
        socket.send(JSON.stringify(msg));

    });

    $("#sendBtn").on("click", function () {

        var msg = {
            cmd: "send-card",
            gameid: $("#gameid").val(),
            playerid: myParam,
            cardID: $("#tablecardid").val()
        };
        console.log(JSON.stringify(msg));
        socket.send(JSON.stringify(msg));
    }
    );

    $("#drawBtn").on("click", function () {

        var msg = {
            cmd: "draw-card",
            gameid: $("#gameid").val(),
            playerid: myParam
        };
        socket.send(JSON.stringify(msg));
    });


    /*
     $(function () {
     var drawtemplate = Handlebars.compile($("#draw-template").html());
     
     $("#draw").append(drawtemplate({did: draws[0]}));
     
     });
     
     
     $(function () {
     
     $("div[data-drag]").draggable();
     });
     */

});

