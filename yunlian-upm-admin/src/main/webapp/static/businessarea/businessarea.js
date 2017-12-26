/**
 * @fileoverview crm-businessarea
 */
M.add('upm-businessarea', function(Y) {
    Y.namespace("mt");

    Y.mt.BusinessArea = {
        _listData: {},
        _dataUrl: "/businessarea/list/all/jsonp?callback={callback}",
        _districtUrl: "/businessarea/list/district/jsonp?callback={callback}",
        _businessAreaUrl: "/businessarea/list/businessarea/jsonp?callback={callback}",

        init: function(cityId, callback) {
            var _this = this;
            if (_this._listData[cityId] == null) {
                _this._initData(cityId, callback);
            } else {
                if (callback) {
                    callback(_this._listData[cityId]);
                }
            }
        },
        _initData: function(cityId, callback) {
            var _this = this;
            var url = _this._dataUrl + "&cityId=" + cityId;

            Y.jsonp(url, function(data) {
                _this._listData[cityId] = data;

                if (callback) {
                    callback(data);
                }
            });
        },
        getDistrict: function(cityId, callback) {
            var _this = this;
            var url = _this._districtUrl + "&cityId=" + cityId;

            Y.jsonp(url, function(data) {
                if (callback) {
                    callback(data);
                }
            });
        },
        getBusinessArea: function(cityId, districtId, callback) {
            var _this = this;
            var url = _this._businessAreaUrl + "&cityId=" + cityId + "&districtId=" + districtId;

            Y.jsonp(url, function(data) {
                if (callback) {
                    callback(data);
                }
            });
        }
    };
}, "1.0.0", {requires: ["jsonp"] });
