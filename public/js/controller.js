var count = 0;
function main(){
    
    if (results.length == 0){
        $("#mainRequests").text("No requests right now");
    }
    for (i = 0; i < results.length; i ++){
        myObj = results[i]
        appizzle(myObj.tableno, myObj.request, myObj.currenttime, myObj.id)
    }

    io = io.connect();
    io.on('newRow', function(data){
        var tableno = data.tableno
        var request = data.request
        var num = data.numberofrequests
        var time = data.currenttime
        appizzle(tableno, request, time)
    })

    $("li").click(function() {
        getRidOfItem(this.id);
        this.remove();
   });
}

$(document).ready(main);

function getRidOfItem(idVal){
    $.post('http://localhost:3000/api/v1/remove', {
        id: idVal
    }, function(data, status){
        console.log(status);
    })
}

function appizzle(tableNo, request, currenttime, id){
    (currenttime[0] == '0') ? currenttime = currenttime.substring(1, 5) : currenttime = currenttime.substring(0, 5)
    //$("#mainRequests").text("");
    $("#mainRequests").append('<li id=\'' + id + '\'> <b>Table ' + tableNo + ":</b> " + request + " requested at: " + currenttime +  '</li>')
    count++;
}