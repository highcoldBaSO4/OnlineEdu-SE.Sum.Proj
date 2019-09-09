import React, { Component } from 'react';
import { Text, View, TextInput } from 'react-native';
// import Login from './views/Login';
// import { createStackNavigator, createAppContainer } from 'react-navigation';

// const navigator = createStackNavigator({
//     Login: {
//         screen: Login
//     }
// });
//
// const App = createAppContainer(navigator);
//
// export default App;

export default class HelloWorldApp extends Component {
  render() {
    return (
        <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
          <Text>Hello, world!</Text>
          <Text>我是谁？你看不见我</Text>
          <TextInput style={{height: 40, borderStyle: "solid"}} placeholder={"hahaha"}/>
          <Text>ah</Text>
        </View>
    );
  }
}

