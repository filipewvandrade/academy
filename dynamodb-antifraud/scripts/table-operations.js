module.exports = {
    createTable: createTable,
    deleteTable: deleteTable,
    describeTable: describeTable
}

function createTable() {
    var params = {
        AttributeDefinitions: [
            {
                AttributeName: 'customer_reference',
                AttributeType: 'S'
            },
            {
                AttributeName: 'year_month_day_hh_mm_ss',
                AttributeType: 'S'
            },
<<<<<<< HEAD
           {
=======
            {
>>>>>>> 6448581 (Adds content)
                AttributeName: 'epoch_time',
                AttributeType: 'N'
            },
        ],
        KeySchema: [
            {
                AttributeName: 'customer_reference',
                KeyType: 'HASH'
            },
            {
                AttributeName: 'year_month_day_hh_mm_ss',
                KeyType: 'RANGE'
            },
        ],
        TableName: global.tableName,
        GlobalSecondaryIndexes: [
<<<<<<< HEAD
<<<<<<< HEAD
        ],
=======
       ],
>>>>>>> 40105ef (Adds content)
=======
       ],
=======
            
        ],
>>>>>>> 6448581 (Adds content)
>>>>>>> 1c66760 (Adds content)
        LocalSecondaryIndexes: [
            {
                IndexName: 'customer_reference_epoch_time_index',
                KeySchema: [
                    {
                        AttributeName: 'customer_reference',
                        KeyType: 'HASH'
                    },
                    {
                        AttributeName: 'epoch_time',
                        KeyType: 'RANGE'
                    },
                ],
                Projection: {
                    ProjectionType: 'ALL'
                }
            }
        ],
        ProvisionedThroughput: {
            ReadCapacityUnits: 5,
            WriteCapacityUnits: 5
        }
    };
    dynamodb.createTable(params, function (err, data) {
        if (err) console.log(err, err.stack); // an error occurred
        else console.log(data);           // successful response
    });
}

function deleteTable() {
    dynamodb.deleteTable({
        TableName: global.tableName
    }, (err, data) => {
        if (err) {
            console.log(err);
        } else {
            console.log(JSON.stringify(data, null, 2));
        }
    });
}

function describeTable() {
    dynamodb.describeTable({
        TableName: global.tableName
    }, (err, data) => {
        if (err) {
            console.log(err);
        } else {
            console.log(JSON.stringify(data, null, 2));
        }
    });
}


