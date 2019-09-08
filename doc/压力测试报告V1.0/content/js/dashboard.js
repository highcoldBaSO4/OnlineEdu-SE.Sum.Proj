/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 6;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 99.99565217391304, "KoPercent": 0.004347826086956522};
    var dataset = [
        {
            "label" : "KO",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "OK",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.48432608695652174, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.466, 500, 1500, "HTTP\u8BF7\u6C4212"], "isController": false}, {"data": [0.566, 500, 1500, "HTTP\u8BF7\u6C4223"], "isController": false}, {"data": [0.462, 500, 1500, "HTTP\u8BF7\u6C4213"], "isController": false}, {"data": [0.6205, 500, 1500, "HTTP\u8BF7\u6C4224"], "isController": false}, {"data": [0.4685, 500, 1500, "HTTP\u8BF7\u6C4214"], "isController": false}, {"data": [0.4535, 500, 1500, "HTTP\u8BF7\u6C4215"], "isController": false}, {"data": [0.5015, 500, 1500, "HTTP\u8BF7\u6C4220"], "isController": false}, {"data": [0.459, 500, 1500, "HTTP\u8BF7\u6C42"], "isController": false}, {"data": [0.452, 500, 1500, "HTTP\u8BF7\u6C4210"], "isController": false}, {"data": [0.538, 500, 1500, "HTTP\u8BF7\u6C4221"], "isController": false}, {"data": [0.4525, 500, 1500, "HTTP\u8BF7\u6C4211"], "isController": false}, {"data": [0.574, 500, 1500, "HTTP\u8BF7\u6C4222"], "isController": false}, {"data": [0.4915, 500, 1500, "HTTP\u8BF7\u6C422"], "isController": false}, {"data": [0.5005, 500, 1500, "HTTP\u8BF7\u6C4216"], "isController": false}, {"data": [0.5085, 500, 1500, "HTTP\u8BF7\u6C4217"], "isController": false}, {"data": [0.496, 500, 1500, "HTTP\u8BF7\u6C4218"], "isController": false}, {"data": [0.529, 500, 1500, "HTTP\u8BF7\u6C4219"], "isController": false}, {"data": [0.4485, 500, 1500, "HTTP\u8BF7\u6C429"], "isController": false}, {"data": [0.424, 500, 1500, "HTTP\u8BF7\u6C427"], "isController": false}, {"data": [0.4265, 500, 1500, "HTTP\u8BF7\u6C428"], "isController": false}, {"data": [0.421, 500, 1500, "HTTP\u8BF7\u6C425"], "isController": false}, {"data": [0.415, 500, 1500, "HTTP\u8BF7\u6C426"], "isController": false}, {"data": [0.4655, 500, 1500, "HTTP\u8BF7\u6C423"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 23000, 1, 0.004347826086956522, 1468.0025217391262, 5, 54342, 3737.9000000000015, 4503.9000000000015, 6175.950000000008, 331.54586865017586, 1281.205136010386, 126.16844861309318], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Transactions\/s", "Received", "Sent"], "items": [{"data": ["HTTP\u8BF7\u6C4212", 1000, 0, 0.0, 1472.6150000000002, 7, 7921, 3607.6999999999994, 4236.799999999999, 5492.860000000001, 15.378937007874017, 6.593118502399114, 5.7220458984375], "isController": false}, {"data": ["HTTP\u8BF7\u6C4223", 1000, 0, 0.0, 1148.0899999999988, 18, 10581, 3021.2, 3652.5999999999995, 5488.210000000001, 19.01502186727515, 173.61929400789123, 7.167771914812701], "isController": false}, {"data": ["HTTP\u8BF7\u6C4213", 1000, 0, 0.0, 1435.6709999999991, 6, 7146, 3407.2, 4056.85, 5532.97, 15.590165723461642, 5.648390120511981, 6.074683714512886], "isController": false}, {"data": ["HTTP\u8BF7\u6C4224", 1000, 0, 0.0, 976.2740000000003, 9, 8097, 2695.9, 3439.9499999999985, 4994.96, 19.25743336928054, 19.595942940224926, 7.240343600754891], "isController": false}, {"data": ["HTTP\u8BF7\u6C4214", 1000, 0, 0.0, 1396.5450000000005, 5, 7382, 3274.0, 3935.5499999999993, 5376.67, 15.589436597761358, 5.648125954852992, 6.074399611823028], "isController": false}, {"data": ["HTTP\u8BF7\u6C4215", 1000, 0, 0.0, 1497.3930000000016, 6, 7981, 3424.2, 4363.8499999999985, 5582.76, 15.95150741745095, 5.779305910033498, 6.324523448715904], "isController": false}, {"data": ["HTTP\u8BF7\u6C4220", 1000, 0, 0.0, 1424.8769999999993, 35, 11203, 3398.7, 4038.2999999999993, 6987.52, 17.729867735186698, 577.3349820263466, 6.769900668416014], "isController": false}, {"data": ["HTTP\u8BF7\u6C42", 1000, 0, 0.0, 1461.1059999999995, 72, 9345, 3436.8, 4401.499999999998, 6527.270000000001, 99.9000999000999, 842.6756836913087, 37.16986138861139], "isController": false}, {"data": ["HTTP\u8BF7\u6C4210", 1000, 0, 0.0, 1636.666, 11, 29466, 3758.8, 4383.549999999998, 5868.26, 26.34768403857301, 66.89841650418929, 10.11195295621015], "isController": false}, {"data": ["HTTP\u8BF7\u6C4221", 1000, 1, 0.1, 1277.2150000000006, 18, 21001, 3204.7, 4077.7499999999995, 6055.850000000002, 18.333822235259607, 154.52925261600726, 6.8146494974240985], "isController": false}, {"data": ["HTTP\u8BF7\u6C4211", 1000, 0, 0.0, 1593.6890000000037, 16, 54342, 3728.7, 4189.799999999999, 5443.620000000001, 15.338834862103875, 59.72259237813295, 5.976753037089303], "isController": false}, {"data": ["HTTP\u8BF7\u6C4222", 1000, 0, 0.0, 1126.3100000000002, 9, 7062, 2968.7, 3689.049999999999, 6168.39, 18.339201877934272, 6.64437880538439, 6.895110081059272], "isController": false}, {"data": ["HTTP\u8BF7\u6C422", 1000, 0, 0.0, 1394.2540000000008, 12, 10069, 3276.5, 4039.249999999999, 5702.170000000002, 79.75753708725475, 48.758025602169404, 29.831188187908758], "isController": false}, {"data": ["HTTP\u8BF7\u6C4216", 1000, 0, 0.0, 1283.6099999999992, 6, 6441, 3181.899999999999, 3807.3499999999976, 5309.9400000000005, 15.98976654940838, 11.305264630636392, 5.886857411256796], "isController": false}, {"data": ["HTTP\u8BF7\u6C4217", 1000, 0, 0.0, 1308.7979999999993, 10, 13230, 3250.8, 3924.5499999999993, 5373.85, 16.638935108153078, 46.017054908485854, 6.190853785357737], "isController": false}, {"data": ["HTTP\u8BF7\u6C4218", 1000, 0, 0.0, 1305.3589999999983, 8, 9037, 3236.2999999999997, 3820.8999999999996, 5556.96, 16.935374610486384, 6.086150250643544, 6.483073093076818], "isController": false}, {"data": ["HTTP\u8BF7\u6C4219", 1000, 0, 0.0, 1254.5130000000008, 6, 7405, 3239.3999999999996, 3994.6499999999996, 6301.960000000003, 16.933367200067735, 6.085428837524342, 6.465768139869613], "isController": false}, {"data": ["HTTP\u8BF7\u6C429", 1000, 0, 0.0, 1664.6830000000016, 21, 29753, 3767.9, 4545.149999999999, 6649.6, 25.129416494948988, 179.78791950419662, 9.61985475197266], "isController": false}, {"data": ["HTTP\u8BF7\u6C427", 1000, 0, 0.0, 1981.8569999999995, 23, 30849, 4521.099999999999, 5286.299999999999, 7025.340000000002, 26.321330806485577, 79.83794285639082, 9.999021175510634], "isController": false}, {"data": ["HTTP\u8BF7\u6C428", 1000, 0, 0.0, 1915.4730000000022, 11, 31391, 4119.499999999999, 5105.399999999998, 6908.700000000001, 25.875899187496767, 81.5697290793355, 10.057234254515345], "isController": false}, {"data": ["HTTP\u8BF7\u6C425", 1000, 0, 0.0, 1854.2169999999974, 11, 9086, 4439.3, 5171.999999999997, 6759.990000000001, 52.493438320209975, 28.040928477690287, 19.68503937007874], "isController": false}, {"data": ["HTTP\u8BF7\u6C426", 1000, 0, 0.0, 1941.909, 24, 9960, 4506.099999999999, 5232.149999999999, 6797.91, 51.93725979017347, 101.69356042900176, 20.085112184481144], "isController": false}, {"data": ["HTTP\u8BF7\u6C423", 1000, 0, 0.0, 1412.934, 7, 7193, 3195.5, 3958.149999999996, 5775.160000000001, 65.24859715516116, 23.44871460263604, 25.105417264778808], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Percentile 1
            case 8:
            // Percentile 2
            case 9:
            // Percentile 3
            case 10:
            // Throughput
            case 11:
            // Kbytes/s
            case 12:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["Non HTTP response code: org.apache.http.conn.HttpHostConnectException\/Non HTTP response message: Connect to 202.120.40.8:30382 [\\\/202.120.40.8] failed: Connection timed out: connect", 1, 100.0, 0.004347826086956522], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 23000, 1, "Non HTTP response code: org.apache.http.conn.HttpHostConnectException\/Non HTTP response message: Connect to 202.120.40.8:30382 [\\\/202.120.40.8] failed: Connection timed out: connect", 1, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["HTTP\u8BF7\u6C4221", 1000, 1, "Non HTTP response code: org.apache.http.conn.HttpHostConnectException\/Non HTTP response message: Connect to 202.120.40.8:30382 [\\\/202.120.40.8] failed: Connection timed out: connect", 1, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
