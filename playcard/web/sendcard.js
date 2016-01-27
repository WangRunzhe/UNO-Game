$(function ()
{
    var socket = null;

    $("#createBtn").on("click", function () {

        socket = new WebSocket("ws://localhost:8080/playcard/uno/" + $("#gameID").val());

        var refreshHandcard = {
            cmd: "refresh-handcard",
            gameID: $("#gameID").val(),
            playerID: $("#playerID").val()

        };

        socket.onopen = function () {
            $("#Status").text("Game prepared!");
        };

        socket.onmessage = function (evt) {

            $("#Handcards").text("Handcards:");
            var msg = JSON.parse(evt.data);//msg 为手牌
            switch (msg.cmd) {
                case "start-game":
                    var HandcardArray = new Array();
                    for (var i = 0; i < msg.card.length; i++)
                        HandcardArray[i] = msg.card[i].cardID;
                    $("#nowcard").text(HandcardArray);
                    break;
                case "send-card":
                    var card = msg.card.cardID;
                    $("#showcard").text(card);
                    if ($("#playerID").val() == msg.playerid)
                        socket.send(JSON.stringify(refreshHandcard));
                    break;
                case "draw-card":
                    var HandcardArray = new Array();
                    for (var i = 0; i < msg.card.length; i++)
                        HandcardArray[i] = msg.card[i].cardID;
                    $("#nowcard").text(HandcardArray);
                    break;
                case "refresh-handcard":

                    var HandcardArray = new Array();
                    for (var i = 0; i < msg.card.length; i++)
                        HandcardArray[i] = msg.card[i].cardID;
                    $("#nowcard").text(HandcardArray);
                    break;
                default :
                    break;
            }
        };
    })

    $("#startBtn").on("click", function () {

        var msg = {
            cmd: "start-game",
            gameID: $("#gameID").val(),
            playerID: $("#playerID").val()
        };
        socket.send(JSON.stringify(msg));

    });

    $("#sendBtn").on("click", function () {

        var msg = {
            cmd: "send-card",
            gameID: $("#gameID").val(),
            playerID: $("#playerID").val(),
            cardID: $("#sendCardID").val()
        };
        console.log(JSON.stringify(msg));
        socket.send(JSON.stringify(msg));
    }
    )

    $("#drawBtn").on("click", function () {

        var msg = {
            cmd: "draw-card",
            gameID: $("#gameID").val(),
            playerID: $("#playerID").val()
        };
        socket.send(JSON.stringify(msg));
    });

});


