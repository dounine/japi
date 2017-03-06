
module.exports = function (d) {
    if(!d)return null;
    var ds = '';
    var f = function(name,o) {
        for(var i in o){
            var v = o[i];
            if((typeof v)==='object'){
                f(i,v);
            }else{
                ds +=(name+'.'+i+'='+(v?v:'')+'&');
            }
        }
    }
    for(var i in d){
        var v = d[i];
        if((typeof v)==='object'){
            f(i,v);
        }else{
            ds += (i+'='+v+'&');
        }
    }
    return ds;
}