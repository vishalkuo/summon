var express = require('express.io')
var app = require('express.io')()
app.http().io()
var bodyParser = require('body-parser')
app.set('view engine', 'jade');
var pg = require('pg')
var connString = process.env.DATABASE_URL || 'postgres://localhost:5432/toast_dev'


app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json())
app.use(express.static(__dirname + '/public'));
app.locals.pretty = true;

//INDEX PARSING

app.get('/', function(req, res){
    var results = []
    pg.connect(connString, function(err, client, done){
        var query = client.query("SELECT * FROM restaurantRequests WHERE completed = false");


        query.on('row', function(row) {
            results.push(row);
        });

        query.on('end', function(){
            client.end();
            res.render('index', {title: 'Vishal\'s Program!', data: JSON.stringify(results)})


            console.log("200 Successful GET at api/v1/menuItems");
        });

        if (err){
            console.log(err)
        }
    })
})

app.get('/api/v1/menuItems', function(req, res){
    var results = []
    pg.connect(connString, function(err, client, done){
        var query = client.query("SELECT id, name, description, \"basePrice\" FROM \"MenuItem\" INNER JOIN " 
            + "\"MenuGroup_MenuItems\" on id = item_id GROUP BY id ORDER BY id ASC;")


        query.on('row', function(row) {
            row.name = row.name.replace(/'/g, '')
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
    var dt = new Date();
    var time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
    var data = {tableno: req.body.tableno, request: request, requestCode: requestCode, numberofrequests: 1,
        currenttime: time};
    var results = []
    pg.connect(connString, function(err, client, done){
        var query = client.query('SELECT * FROM restaurantRequests WHERE tableno =' + tableno 
            +' AND requestCode = ' + requestCode + ';')


        query.on('row', function(row) {
            results.push(row);
        });

        query.on('end', function() {
            if (results.length > 0 && requestCode != 0){
                var currentCount = results[0].numberofrequests
                currentCount += 1
                client.query('UPDATE restaurantRequests SET numberofrequests = ' + currentCount
                    + ' WHERE tableno = ' + tableno + ' and requestCode = ' + requestCode + ';');
                data.numberofrequests = currentCount; 
                data.idVal = results[0].id;

                data.update = true;               
                req.io.broadcast('newRow', data);
                client.end()
            }else{
                client.query('INSERT INTO restaurantRequests(tableno, request, requestCode) VALUES '
                     + '(' + tableno + ',\'' + request + '\',' + requestCode + ');');
                
                var idQuery = client.query('SELECT id FROM restaurantRequests WHERE tableno =' + tableno 
            +' AND requestCode = ' + requestCode + ';')
                var test = []
                idQuery.on('row', function(row){
                    test.push(row);
                })

                idQuery.on('end', function(){
                    data.idVal   = test[test.length -1]
                    data.update = false;
                    req.io.broadcast('newRow', data);
                    client.end()
                })
                
            }
            
        });
        if (err){
            console.log(err)
        }else{
            console.log("Successful POST to /api/v1/tableRequest");
        }
    })
    
    
    req.io.respond()
})

app.post('/api/v1/remove', function(req, res){
    var remove = req.body.id
    pg.connect(connString, function(err, client, done){
        client.query("delete from restaurantRequests where id = " + remove + ";");
        client.end

        if (err){
            console.log(err)
        }
    })
})

app.listen(3000)