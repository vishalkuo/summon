var express = require('express.io')
var app = require('express.io')()
app.http().io()
var bodyParser = require('body-parser')
app.set('view engine', 'jade');
var pg = require('pg')
var connString = process.env.DATABASE_URL || 'postgres://localhost:5432/toast_dev'

app.use(bodyParser.urlencoded({extended: false}));
//app.use(bodyParser.json())
app.use(express.static(__dirname + '/public'));

app.get('/', function(req, res){
    res.render('index', {title: 'Vishal\'s Program!'})
})

app.get('/api/v1/menuItems', function(req, res){
    var results = []
    pg.connect(connString, function(err, client, done){
        var query = client.query("SELECT id, name, description FROM \"MenuItem\" INNER JOIN " 
            + "\"MenuGroup_MenuItems\" on id = item_id GROUP BY id ORDER BY id ASC;")


        query.on('row', function(row) {
            results.push(row);
        });

        query.on('end', function(){
            client.end();
            res.json(results)
            console.log("200 Successful GET at api/v1/menuItems");
        });

        if (err){
            console.log(err)
        }
    })
})

app.post('/api/v1/tableRequest', function(req,res){
    req.io.route('ioTableRequest')
})

app.io.route('ioTableRequest', function(req){
    var tableno = req.body.tableno
    var request = req.body.request
    var requestCode = req.body.requestCode
    console.log(tableno)
    console.log(requestCode)
    var data = {tableno: req.body.tableno, request: request, requestCode: requestCode, numberofrequests: 1};
    var results = []
    pg.connect(connString, function(err, client, done){
        var query = client.query('SELECT * FROM restaurantRequests WHERE tableno =' + tableno 
            +' AND requestCode = ' + requestCode + ';')


        query.on('row', function(row) {
            results.push(row);
        });

        query.on('end', function() {
            if (results.length > 0){
                var currentCount = results[0].numberofrequests
                currentCount += 1
                client.query('UPDATE restaurantRequests SET numberofrequests = ' + currentCount
                    + ' WHERE tableno = ' + tableno + ' and requestCode = ' + requestCode + ';');
                console.log("Successful POST to /api/v1/tableRequest");
                data.numberofrequests = currentCount;
                
            }else{
                client.query('INSERT INTO restaurantRequests(tableno, request, requestCode) VALUES '
                     + '(' + tableno + ',\'' + request + '\',' + requestCode + ');');
                console.log("Successful POST to /api/v1/tableRequest");
                
            }
            client.end()
        });
        if (err){
            console.log(err)
        }
    })
    
    req.io.broadcast('newRow', data);
    req.io.respond()
})

app.listen(3000)