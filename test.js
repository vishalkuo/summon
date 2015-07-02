

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
var j5 = require("johnny-five");
var board = new j5.Board();
 
var LEDPIN = 13;
var OUTPUT = 1;
 
board.on("ready", function(){
  var val = 0;
 
  // Set pin 13 to OUTPUT mode
  this.pinMode(LEDPIN, OUTPUT);
 
  // Create a loop to "flash/blink/strobe" an led
  this.loop( 100, function() {
    this.digitalWrite(LEDPIN, (val = val ? 0 : 1));
  });
});	
