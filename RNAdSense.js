import React, { Component } from 'react';
import {
  requireNativeComponent,
  UIManager,
  findNodeHandle,
  ViewPropTypes,
} from 'react-native';
import { bool, string, func, arrayOf } from 'prop-types';

import { createErrorFromErrorData } from './utils';

class AdSense extends Component {

  constructor() {
    super();
    this.handleSizeChange = this.handleSizeChange.bind(this);
    this.handleAppEvent = this.handleAppEvent.bind(this);
    this.handleAdFailedToLoad = this.handleAdFailedToLoad.bind(this);
    this.state = {
      style: {},
    };
  }

  componentDidMount() {
    this.loadBanner();
  }

  loadBanner() {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this._bannerView),
      UIManager.RNAdSenseView.Commands.loadBanner,
      null,
    );
  }

  handleSizeChange(event) {
    const { height, width } = event.nativeEvent;
    this.setState({ style: { width, height } });
    if (this.props.onSizeChange) {
      this.props.onSizeChange({ width, height });
    }
  }

  handleAppEvent(event) {
    if (this.props.onAppEvent) {
      const { name, info } = event.nativeEvent;
      this.props.onAppEvent({ name, info });
    }
  }

  handleAdFailedToLoad(event) {
    if (this.props.onAdFailedToLoad) {
      this.props.onAdFailedToLoad(createErrorFromErrorData(event.nativeEvent.error));
    }
  }

  render() {
    return (
      <RNAdSenseView
        {...this.props}
        style={[this.props.style, this.state.style]}
        onSizeChange={this.handleSizeChange}
        onAdFailedToLoad={this.handleAdFailedToLoad}
        onAppEvent={this.handleAppEvent}
        ref={el => (this._bannerView = el)}
      />
    );
  }
}

Object.defineProperty(AdSense, 'simulatorId', {
  get() {
    return UIManager.RNAdSenseView.Constants.simulatorId;
  },
});

AdSense.propTypes = {
  ...ViewPropTypes,

  /**
										  
													 
															
															   
																		   
														  
													  
																					   
																						
	
					  
	 
				 

	 
																				  
	 
								

	 
   * DFP ad unit ID
   */
  adUnitID: string,

/**
   * adtest
   */
  adTest: string,

  /**
   * number
   */
  number: string,
  container: string,
  lines: string,
  width: string,
  fontFamily: string,
  fontSizeTitle: string,
  fontSizeDescription: string,
  fontSizeDomainLink: string,
  colorTitleLink: string,
  colorText: string,
  colorDomainLink: string,
  colorBackground: string,
  colorAdBorder: string,
  colorBorder: string,
  noTitleUnderline: string,
  detailedAttribution: string,
  longerHeadlines: string,
  query: string,
  /**
																	 
	 
							   

					 

	 
   * DFP library events
   */
  onAdLoaded: func,
  onAdFailedToLoad: func,
  onAdOpened: func,
  onAdClosed: func,
  onAdLeftApplication: func,
  onAppEvent: func,
};

const RNAdSenseView = requireNativeComponent('RNAdSenseView', AdSense);

export default AdSense;
