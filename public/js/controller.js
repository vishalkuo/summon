function main(){
    for (i = 0; i < results.length; i ++){
        myObj = results[i]
        appizzle(myObj.tableno, myObj.request, myObj.currenttime)
    }

    io = io.connect();
    io.on('newRow', function(data){
        var tableno = data.tableno
        var request = data.request
        var num = data.numberofrequests
        var time = data.currenttime
        appizzle(tableno, request, time)
    })
}

$(document).ready(main);

function appizzle(tableNo, request, currenttime){
    currenttime = (new Date).getTime()

    $("#mainRequests").append('<li> Table: ' + tableNo + ", request: " + request + " at: " + currenttime +  '</li>')
}