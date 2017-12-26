/**
 * @fileoverview crm-category
 */
M.add('upm-category', function(Y) {
    Y.namespace("mt");

    Y.mt.Category = {
        _listData: null,
        _mapData: null,
        _showAll: true,
        _dataUrl: "/cat/list/jsonp?callback={callback}",

        init: function(callback, params) {
            var _this = this;

            if(typeof params !== 'undefined' && typeof params.showAll !== 'undefined') {
                _this._showAll = params["showAll"];
            } else {
                _this._showAll = true;
            }

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
                if(_this._showAll) {
                    data.unshift({id: "-2", name: "全部", parentId: "0"});
                }
                _this._listData = data;

                var mapData = {};
                for (var i = 0,n = data.length; i < n; i++) {
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
        getFullCatDesc: function(catId) {
            /*jshint eqeqeq:false, -W041:false*/
            var _this = this;

            if (catId == 0) {
                return "";
            } else {
                return _this._getFullCatDesc(catId);
            }
        },
        _getFullCatDesc: function(catId) {
            /*jshint eqeqeq:false, -W041:false*/
            var _this = this;
            var fullCatDesc = [];

            fullCatDesc.unshift(_this._mapData[catId]["name"]);

            while (catId != null && (catId = _this._mapData[catId]["parentId"]) != 0) {
                fullCatDesc.unshift(_this._mapData[catId]["name"]);
            }

            return fullCatDesc.join("-");
        }
    };
}, "1.0.0", {requires: [
    'jsonp'
] });
