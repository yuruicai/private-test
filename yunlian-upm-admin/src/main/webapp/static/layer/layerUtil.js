/**
 * Created by zhanggm on 2015/11/24.
 */

//提示成功信息
function alertSuccess(msg){
    layer.alert(msg, {
        icon: 1,
        skin: 'layer-ext-moon'
    });
}

//提示成功信息,带回调函数
function alertSuccess(msg,methodObj){
    layer.alert(msg, {
        icon: 1,
        skin: 'layer-ext-moon'
    },methodObj);
}

//提示失败信息
function alertError(msg){
    layer.alert( msg, {
        icon: 2,
        skin: 'layer-ext-moon'
    });
}

//提示失败信息,带回调函数
function alertError(msg,methodObj){
    layer.alert( msg, {
        icon: 2,
        skin: 'layer-ext-moon'
    },methodObj);
}
