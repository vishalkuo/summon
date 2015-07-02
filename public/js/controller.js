var count = 0;
function main(){

    for (i = 0; i < results.length; i ++){
        myObj = results[i] 
        appizzle(myObj.tableno, myObj.request, myObj.currenttime, myObj.id, myObj.update, myObj.numberofrequests)
    }

    io = io.connect();
    io.on('newRow', function(data){
        var tableno = data.tableno
        var request = data.request
        var num = data.numberofrequests
        var time = data.currenttime
        var id = data.idVal;
        var update = data.update;
        var numberofrequests = data.numberofrequests
        appizzle(tableno, request, time, id, update, numberofrequests)
        if (data.newVal == true){
            console.log('here');
        }
        var idizzle = "#" + id;

    })

    $("li").click(function() {
        getRidOfItem(this.id);
        this.remove();
   });
    $('body').on('click', 'li', function() {
    // do something
        getRidOfItem(this.id);
        this.remove();
    });

    $('li').on('change', 'li', function(){
            console.log("here")
    })
    
}

$(document).ready(main);




function getRidOfItem(idVal){
    $.post('http://localhost:3000/api/v1/remove', {
        id: idVal
    }, function(data, status){
        console.log(status);
    })
}

function appizzle(tableNo, request, currenttime, idVal, update, requestnum){

    currenttime = currenttime.slice(0, 5);
    if (update == true){
        var idizzle = "#" + idVal;
         /*$(idizzle).html('<b>Table ' + tableNo + ":</b> " + request + " requested at: " + currenttime +  
            '. Number of requests: ' + requestnum)

         $('li').trigger('change')*/

         $('li').each(function(index){
            if(this.id == idVal){
                $(idizzle).html('<b>Table ' + tableNo + ":</b> " + request + " requested at: " + currenttime +  
            '. Number of requests: ' + requestnum)
            }else if(this.id == '[object Object]'){
                $(this).html('<b>Table ' + tableNo + ":</b> " + request + " requested at: " + currenttime +  
            '. Number of requests: ' + requestnum)
            }
         })
         
    }else{
        $("#mainRequests").append('<li id=\'' + idVal + '\'> <b>Table ' + tableNo + ":</b> " + request + " requested at: " + currenttime +  
            '. Number of requests: ' + requestnum + ' </li>')
    }
    
    count++;
}