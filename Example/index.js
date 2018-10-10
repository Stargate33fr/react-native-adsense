import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Platform,
  TouchableHighlight,
  Button,
  ScrollView,
} from 'react-native';

import AdSense from 'react-native-adsense';

const BannerExample = ({ style, title, children, ...props }) => (
  <View {...props} style={[styles.example, style]}>
    <Text style={styles.title}>{title}</Text>
    <View>
      {children}
    </View>
  </View>
);

const bannerWidths = [200, 250, 320];

export default class Example extends Component {

  constructor() {
    super();
    this.state = {
      fluidSizeIndex: 0,
    };
  }

  render() {
    return (
      <View style={styles.container}>
        <ScrollView>
          <BannerExample title="AdMob - Basic">
            <AdSense
              adUnitID="pub-3902876258354218"
              adTest= "false"
              number= "3"
              lines= "3"
              width= "auto"
              fontFamily= "arial"
              fontSizeTitle= "17"
              fontSizeDescription= "15"
              fontSizeDomainLink= "17"
              colorTitleLink= "#0A4BDA"
              colorText= "#323232"
              colorDomainLink= "#23B60E"
              colorBackground= "#E0DFDF"
              colorAdBorder= "#ffffff"
              colorBorder= "#FAFAFC"
              noTitleUnderline= "false"
              longerHeadlines= "false"
              detailedAttribution= "true"
              query="Linge de Maison   Couette et Oreiller  Oreiller "
              ref={el => (this._basicExample = el)}
            />
            <Button
              title="Reload"
              onPress={() => this._basicExample.loadBanner()}
            />
          </BannerExample>
        </ScrollView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    marginTop: (Platform.OS === 'ios') ? 30 : 10,
  },
  example: {
    paddingVertical: 10,
  },
  title: {
    margin: 10,
    fontSize: 20,
  },
});

AppRegistry.registerComponent('Example', () => Example);
