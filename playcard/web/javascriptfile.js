

/* global Handlebars */
cards = ["c0_00", "c0_01"];//手上
draws = ["c2_00","c2_01"];//牌堆
desk=["","",];//桌上


$(function () {
    var cardtemplate = Handlebars.compile($("#card-template").html());


    for (var i = 0; i < cards.length; i++) {
        console.log(cards[i]);
        $("#cards").append(cardtemplate({cid: cards[i]}));
    }
});


$(function () {
    var drawtemplate = Handlebars.compile($("#draw-template").html());
   
        $("#draw").append(drawtemplate({did: draws[0]}));
    
});


$(function () {

    $("div[data-drag]").draggable()

    $("#droppable").droppable({
        drop: function (event, ui) {
            
            
            decks.push(cid);

        }
    });
});
