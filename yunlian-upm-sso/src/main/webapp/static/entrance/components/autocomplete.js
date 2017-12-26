/**
 * @jsx React.DOM
 */
/*jshint trailing:false, indent:false, unused:false*/
var util = require('util');
var React = global.React || require('react');
var insertCss = require('insert-css');
var fs = require('fs');

function noop() {}

function getLabel(component) {
    return component.props.label || component.props.children;
}

var AC = React.createClass({displayName: 'AC',
    getDefatulProps: function() {
        return {
            autocomplete: 'both',
            onInput: noop,
            onSelect: noop
        };
    },
    getInitialState: function() {
        return {
            value: this.props.value,
            inputValue: this.getInputValue(this.props.value),
            isOpen: false,
            isInputFocused: false,
            isMouseIn: false,
            datalist: this.props.children
        };
    },
    getInputValue: function(value) {
        var inputValue;
        React.Children.forEach(this.props.children, function(child) {
            if (child.props['data-id'] === value) {
                inputValue = getLabel(child);
            }
        }.bind(this));
        return inputValue;
    },
    filterDataList: function(inputValue) {
        var datalist = [];
        React.Children.forEach(this.props.children, function(child) {
            if (child.props.loginName.indexOf(inputValue) !== -1 ||
                child.props.name.indexOf(inputValue) !== -1) {
                datalist.push(child);
            }
        }.bind(this));
        return datalist;
    },
    handleChange: function(event) {
        var inputValue = event.target.value;
        var datalist = this.filterDataList(inputValue);
        this.setState({
            inputValue: inputValue,
            datalist: datalist
        });
    },
    handleFocus: function() {
        this.setState({ isInputFocused: true, isOpen: true });
    },
    handleBlur: function() {
        this.setState({ isInputFocused: false });
        if (!this.state.isMouseIn) {
            this.setState({ isOpen: false });
        }
    },
    handleMouseEnter: function(event) {
        this.setState({ isMouseIn: true });
    },
    handleMouseLeave: function(event) {
        this.setState({ isMouseIn: false });
        if (this.state.isInputFocused) {
            return;
        }
        this.setState({ isOpen: false });
    },
    handleClick: function(event) {
        var liNode = event.target;
        this.state.clicked = true;
        var id = liNode.getAttribute('data-id');
        id = parseInt(id, 10);
        this.setState({
            value: id,
            inputValue: this.getInputValue(id)
        });
    },
    getClassName: function() {
        var cls = [ 'combobox' ];
        if (this.state.isOpen) {
            cls.push('is-expanded');
        } else {
            cls.push('is-collapsed');
        }
        return cls.join(' ');
    },
    render: function() {
        var list = null;
        if (this.state.datalist.length > 0) {
            list = (
                React.DOM.ul({
                    className: "combobox-list", 
                    onClick: this.handleClick
                }, this.state.datalist)
            );
        } else {
            list = (
                React.DOM.p({className: "combobox-list"}, "没有找到符合条件的用户")
            );
        }
        return (
            React.DOM.div({
                className: this.getClassName(), 
                onMouseEnter: this.handleMouseEnter, 
                onFocus: this.handleFocus, 
                onBlur: this.handleBlur, 
                onMouseLeave: this.handleMouseLeave
            }, 
                React.DOM.input({
                    type: "text", 
                    className: "combobox-input", 
                    value: this.state.inputValue, 
                    onChange: this.handleChange}
                ), 
                React.DOM.input({
                    type: "hidden", 
                    name: this.props.name, 
                    value: this.state.value}
                ), 
                list
            )
        );
    },
    componentDidMount: function() {
        var css = fs.readFileSync(__dirname + '/autocomplete.css', { encoding: 'utf8' });
        insertCss(css);
    }
});

AC.normalizeChildren = function(datalist) {
    if (util.isArray(datalist)) {
        datalist = datalist.map(function(item) {
            return (
                React.DOM.li({
                    id: item.id, 
                    key: item.id, 
                    name: item.name, 
                    loginName: item.loginName, 
                    'data-id': item.id
                }, 
                    item.name + ' - ' + item.loginName
                )
            );
        });
    }
    return datalist;
};

module.exports = AC;
