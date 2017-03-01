var config = require('../config.json');

module.exports = function (getPro) {
    if(getPro){//是否获取判断是否是生产环境
        return config['default']=='pro'?true:false;
    }
    return config[config.default];
}