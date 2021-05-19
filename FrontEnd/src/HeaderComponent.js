import React, { Component } from "react";
import "@wk/css";
import "./styles/Header.css";
import "@wk/components/dist/header.min.css";

class Header extends Component {
  render() {
    return (
      <div className="wk-docs-research-example">
        <header className="wk-banner wk-banner-extra-margins" role="banner">
          <div className="wk-banner-container">
            <div className="wk-banner-content">
              <div className="wk-banner-left-content" style={{ margin: 30 }}>
                <a className="wk-logo">
                  <img
                    src="//cdn.wolterskluwer.io/wk/logos/1.1.x/wk-brand.svg"
                    alt="CCH® AnswerConnect"
                    className="wk-logo-large"
                  />
                </a>
              </div>
              <div className="wk-banner-right-content">
                <div className="wk-product-brand">
                  <span className="product-app-name">XML Transformation</span>
                </div>
              </div>
            </div>
          </div>
        </header>
      </div>
    );
  }
}
export default Header;
