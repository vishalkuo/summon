var app = require('express.io')()
app.http().io()
var bodyParser = require('body-parser')
app.set('view engine', 'jade');
var pg = require('pg')
var connString = process.env.DATABASE_URL || 'postgres://localhost:5432/toast_dev'

app.use(bodyParser.urlencoded({extended: false}));


app.get('/', function(req, res){
    res.render('index', {title: 'Vishal\'s Program!'})
})

app.get('/api/v1/menuItems', function(req, res){
    var results = []
    pg.connect(connString, function(err, client, done){
        var query = client.query("SELECT id, name, description FROM \"MenuItem\" INNER JOIN " 
            + "\"MenuGroup_MenuItems\" on id = item_id GROUP BY id ORDER BY id ASC;")


        query.on('row', function(row){
            results.push(row)
        })

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

app.listen(3000)