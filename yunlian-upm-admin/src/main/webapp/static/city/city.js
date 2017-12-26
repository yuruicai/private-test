/**
 * @fileoverview crm-city
 */
M.add('upm-city', function(Y) {
    Y.namespace("mt");

    Y.mt.City = {
        _listData: null,
        _mapData: null,
        _dataUrl: "/city/list/jsonp?callback={callback}",

        init: function(callback) {
            var _this = this;
            if (_this._listData == null) {
                _this._initData(callback);
            } else {
                if(callback) {
                    callback(_this._listData);
                }
            }
        },
        _initData: function(callback) {
            var _this = this;

            Y.jsonp(_this._dataUrl, function(data) {
                _this._listData = data;

                var mapData = {};
                /*jshint eqeqeq:false,-W041:false*/
                for (var i = 0,n = data.length; i < n; i++) {
                    if(data[i]["id"] == 0) {
                        data[i]["id"] = -1;
                    }
                    mapData[data[i]["id"]] = data[i];
                }
                _this._mapData = mapData;

                if (callback) {
                    callback(data);
                }
            });
        },
        getData: function() {
            return this._listData;
        },
        getCityDesc: function(cityId) {
            return this._mapData[cityId]["name"];
        }
    };

}, "1.0.0", { requires: ["jsonp"] });
