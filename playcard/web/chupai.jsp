

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>test</title>
        <style type="text/css">
            #onhand, #ondeck,#drawcard
            {float:left; width:400px; height:800px; margin:10px;padding:10px;border:1px solid #aaaaaa;}
        </style>

        <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
        <script src="lib/handlebars-v4.0.5.js"></script>
        <script src="lib/jquery-2.2.0.js"></script>
        <script src="javascriptfile.js"></script>
        <link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">



        <script id="card-template" type="application/x-handlebars">
            <div data-drag id={{cid}} class="ui-widget-content" 
            style="width: 170px; height: 256px">
            <img src="uno_deck/{{cid}}.png" draggable="true" 
            ondragstart="drag(event)" id={{cid}} />
            </div>
        </script>

        <script id="draw-template" type="application/x-handlebars">
            <div data-drag id={{did}} class="ui-widget-content" 
            style="width: 170px; height: 256px">
            <img src="uno_deck/back.png" draggable="true" 
            ondragstart="drag(event)" id={{did}} />
            </div>
        </script>

        <script type="text/javascript">
function allowDrop(ev)
{
    ev.preventDefault();
}

function drag(ev)
{
    ev.dataTransfer.setData("Text", ev.target.id);
}

function drop(ev)
{
    ev.preventDefault();


    var data = ev.dataTransfer.getData("Text");
    var msg = {
        cmd: "send-card",
        gameid: $("#gameid").val(),
        playerid: myParam,
        cardID: data
    };
    console.log(JSON.stringify(msg));
    socket.send(JSON.stringify(msg));


    $("#tablecard").attr("src", "uno_deck/" + data + ".png");
    $('#' + data).fadeOut('fast');
}

function draw(ev)
{
    ev.preventDefault();
    var data = ev.dataTransfer.getData("Text");

    var drawcard = Handlebars.compile($("#card-template").html());
    $("#drawcards").append(drawcard({cid: data}));
    var msg = {
        cmd: "draw-card",
        gameid: $("#gameid").val(),
        playerid: myParam
    };
    socket.send(JSON.stringify(msg));
}
        </script>




    </head>
    <body>

        <div id="onhand" ondrop="draw(event)" ondragover="allowDrop(event)">

            Cards On Hand: 

            <div id="cards"></div>
            <div id="drawcards"></div>


        </div>


        <div id="ondeck" ondrop="drop(event)" ondragover="allowDrop(event)">

            Deck: <br>
            <img id="tablecard" src="">
        </div>

        <div id="drawcard" ondrop="draw(event)" ondragover="allowDrop(event)">
            Draw: <br>
            <div id="draw">
            </div>
        </div>










    </body>
</html>
