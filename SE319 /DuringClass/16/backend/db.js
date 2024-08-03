    //Author: Erroll Barker
    //ISU Netid : errollb@iastate.edu
    //Date :  4/26, 2024

const mysql = require('mysql2/promise')
const db = mysql. createPool({
host: "127.0.0.1",
user: "root",
password: "qwedcxzaS",
database: "secoms319"
})
module.exports = db;