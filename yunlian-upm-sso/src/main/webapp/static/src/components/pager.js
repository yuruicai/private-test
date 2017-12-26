/**
 * @jsx React.DOM
 */
/*jshint trailing:false*/
var fs = require('fs');
var url = require('url');
var querystring = require('querystring');

var React = global.React || require('react');
var insertCss = require('insert-css');

var cssContents = fs.readFileSync(__dirname + '/pager.css', { encoding: 'utf8' });

function range(start, stop) {
    if (arguments.length <= 1) {
        stop = start || 0;
        start = 0;
    }

    var length = Math.max(stop - start, 0);
    var idx = 0;
    var arr = new Array(length);

    while(idx < length) {
        arr[idx++] = start;
        start += 1;
    }

    return arr;
}

function normalize(page) {
    page.numPages = page.totalPageCount;
    page.maxPages = page.pageSize;
    page.page = page.pageNo;
    if (typeof page.uriTemplate === 'undefined' && location) {
        page.uriTemplate = parsePageNo(location.href);
    }
    return page;
}

function parsePageNo(uri) {
    var opts = url.parse(uri);
    var query;
    if (opts.query) {
        query = querystring.parse(opts.query);
        if (query.hasOwnProperty('pageNo')) {
            delete query.pageNo;
        }
        query = querystring.stringify(query);
    } else {
        query = '';
    }
    if (query !== '') {
        query += '&';
    }
    query += 'pageNo={page}';
    opts.query = query;
    opts.search = '?' + opts.query;
    return url.format(opts);
}

var Pager = React.createClass({
    statics: {
        normalize: normalize
    },
    propTypes: {
        numPages: React.PropTypes.number.isRequired,
        maxPages: React.PropTypes.number,
        onClick: React.PropTypes.func
    },
    getDefaultProps: function() {
        return {
            maxPages: 3
        };
    },
	getInitialState: function() {
		return {
			page: this.props.page
		};
	},
    preventDefault: function(e) {
        e.preventDefault();
    },
    handleClick: function(n, e) {
        if (n < 1 || n > this.props.numPages) {
            return;
        }
        if (this.props.onClick) {
            this.props.onClick(n, e);
        }
        this.setState({ page: n });
    },
    getDisplayCount: function() {
        if (this.props.numPages > this.props.maxPages) {
            return this.props.maxPages;
        }
        return this.props.numPages;
    },
    getPageRange: function() {
        var displayCount = this.getDisplayCount();
        var page = this.state.page;
        var idx = (page - 1) % displayCount;
        var start = page - idx;
        var remaining = this.props.numPages - page;
        if (page > displayCount && remaining < displayCount) {
            // add 1 due to the implementation of `range`
            start = this.props.numPages - displayCount + 1;
        }
        return range(start, start + displayCount);
    },
    renderPage: function(n, i) {
        var cls = this.state.page === n ? 'active' : '';
        return (
            <li key={i} className={cls} onClick={this.handleClick.bind(this, n)}>
                <a href={this.props.uriTemplate.replace('{page}', n)}>{n}</a>
            </li>
        );
    },
    render: function() {
        var page = this.state.page;
		var prevClassName = page === 1 ? 'disabled' : '';
		var nextClassName = page >= this.props.numPages ? 'disabled' : '';
        var pageClassName = 'inline unstyled pagination';
        if (this.props.align) {
            pageClassName += ' pagination-' + this.props.align;
        }
        var prev = <li className={prevClassName} onClick={this.handleClick.bind(this, page - 1)}>
                <a href={this.props.uriTemplate.replace('{page}', page - 1)}>«</a>
            </li>;
        var next = <li className={nextClassName} onClick={this.handleClick.bind(this, page + 1)}>
                    <a href={this.props.uriTemplate.replace('{page}', page + 1)}>»</a>
                </li>;
        return (
            <ul className={pageClassName}>
                <li><span>共 {this.props.totalCount} 条</span></li>
                {prev}
                {this.getPageRange().map(this.renderPage, this)}
                {next}
            </ul>
        );
    },
    componentDidMount: function() {
        insertCss(cssContents);
    }
});

module.exports = Pager;
