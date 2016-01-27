/* global Handlebars */
$(function () {
//summer added
    $("#individualwaitingdiv").hide();
    $("#ondeck").hide();
    $("#onhand").hide();
    $("#drawcard").hide();
//summer added


    var gameid = "join";
    var socket = new WebSocket("ws://localhost:8080/playcard/game/" + gameid);


    socket.onopen = function () {
        console.log("Connected");
        var listcmd = {
            cmd: "list-of-games"
        };
        socket.send(JSON.stringify(listcmd));
    };

    socket.onmessage = function (evt) {
        console.log("List show--" + evt.data);
        var msgback = JSON.parse(evt.data);
        console.log(msgback);
        console.log(msgback.cmd);

        switch (msgback.cmd) {
            case  "list-of-games":
                console.log("Successfully List All");
                var temp = $("#gameTemplate").html();
                var gameTemplate = Handlebars.compile(temp);
                for (i = 0; i < msgback.games.length; i++) {
                    var $li = {
                        description: " [ Description ]   " + msgback.games[i].description,
                        gameID: "Game ID: " + msgback.games[i].gameID,
                        playernum: msgback.games[i].playernum,
                        gameid: msgback.games[i].gameID
                    };
                    $("#showlist").prepend(gameTemplate($li));
                }
                break;

                //summer added
            case "join-games":
                console.log("join games");
                console.log(msgback);
                $("#idh").text(msgback.gameid);
                $("#pid").text("Player ID: " + msgback.playerid);
                //update here
                $("#JoinedPlayerNum").text("Joined Sequence: " + msgback.joinednum);
                $("#PlayerNum").text("Player Number: " + msgback.playernum);
                break;

            case "start-game":
                console.log("-----comes to start game cmds----------");
                $("#individualwaitingdiv").hide();
                $("#playgamediv").show();
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
                var myParam = location.search.split('playerid=')[1];
                var refreshHandcard = {
                    cmd: "refresh-handcard",
                    gameid: $("#idh").text(),
                    playerid: myParam

                };
                $("#tablecard").attr("src","uno_deck/"+card+".png");
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
    };


    //click on parent id!!!!
    //summer added
    $("#showlist").on("click", "[data-gameid]", function () {
        $("#showlistdiv").hide();
        $("#individualwaitingdiv").show();

        console.log("comes to join click");

        //update 1.26
        var playerNum = $("#playernumdiv").text();
        console.log("Player Number ---from join button>>>>>>>" + playerNum);

        //从button 的data attribute 中拿到game id
        var data_gameid_value = $(this).attr("data-gameid");  //data-* attribute
        console.log(data_gameid_value);

        // 从href的url中拿到playerid
        var myParam = location.search.split('playerid=')[1];
        console.log(myParam);

        var Joincmd = {
            cmd: "join-games",
            gameid: data_gameid_value,
            playerid: myParam,
            PlayerNum: playerNum
        };

        socket.send(JSON.stringify(Joincmd));
    });

    $("#sendBtn").on("click", function () {

        var myParam = location.search.split('playerid=')[1];
        var msg = {
            cmd: "send-card",
            gameid: $("#idh").text(),
            playerid: myParam,
            cardID: $("#tablecardid").val()
        };
        console.log(JSON.stringify(msg));
        socket.send(JSON.stringify(msg));
    }
    );

    $("#drawBtn").on("click", function () {
        var myParam = location.search.split('playerid=')[1];
        var msg = {
            cmd: "draw-card",
            gameid: $("#idh").text(),
            playerid: myParam
        };
        socket.send(JSON.stringify(msg));
    });


//summer added
});



