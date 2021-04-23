import React from "react";
import {StyleSheet, Dimensions} from "react-native";
import {Root} from "native-base";
import store from "./store/store.js";
import TopNav from "./navigations/TopNav.js";
import { Provider } from "react-redux";
import axios from "axios";
import Spinner from "react-native-loading-spinner-overlay";
import MyToast from "./components/MyToast.js"

axios.defaults.withCredentials = true;
axios.defaults.baseURL = "http://202.120.40.8:30382/online-edu";
React.Component.prototype.$axios = axios;
React.Component.prototype.$toast = MyToast;
React.Component.prototype.$window = Dimensions.get('window');

global.showLoading = false;
global.cancelLoading = false;

const styles = StyleSheet.create({
    loadingText: {
        color: "white"
    }
});

const loadingDefaultOptions = {
    textStyle: styles.loadingText,
    animation: "fade"
};

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loadingOptions: {

            }
        };
        global.showLoading = (loadingText, customOption = loadingDefaultOptions) => {
            //alert("MMM");
            this.setState({
                loadingOptions: {
                    visible: true,
                    textContent: loadingText,
                    ...customOption
                }
            });
            console.log(this.state);
        };
        global.cancelLoading = () => {
            this.setState({
                loadingOptions: {}
            })
        };
        this.$axios.interceptors.response.use((response) => {
            global.cancelLoading();
            return response;
        }, (error) => {
            global.cancelLoading();
            console.log(error);
            console.log(error.response);
            if (error.response.status === 401 && error.response.statusText === "Unauthorized") {
                this.$toast.errorToast("登录出错，请重新登录");
            }
            return Promise.reject(error);
        })
    }

    componentDidMount(): void {

    }

    render() {
        return (
            <Provider store={store}>
                <Root>
                    <Spinner {...this.state.loadingOptions} style={{zIndex: 100000}}/>
                    <TopNav/>
                </Root>
            </Provider>
        )
    }
}

export default App;

// import React from "react";
// import { View, Text } from "react-native";
// import { createStackNavigator, createAppContainer } from "react-navigation";
// import LoginScreen from "./screens/LoginScreen"
//
// class HomeScreen extends React.Component {
//   render() {
//     return (
//         <View style={{ flex: 1, alignItems: "center", justifyContent: "center" }}>
//           <Text>Home Screen</Text>
//         </View>
//     );
//   }
// }
//
// const AppNavigator = createStackNavigator({
//     Login: {
//         screen: LoginScreen
//     },
//     Home: {
//         screen: HomeScreen
//     }
// });
//
// export default createAppContainer(AppNavigator);

// /**
//  * Sample React Native App
//  * https://github.com/facebook/react-native
//  *
//  * @format
//  * @flow
//  */
//
// import React, {Fragment} from 'react';
// import {
//   SafeAreaView,
//   StyleSheet,
//   ScrollView,
//   View,
//   Text,
//   StatusBar,
// } from 'react-native';
//
// import {
//   Header,
//   LearnMoreLinks,
//   Colors,
//   DebugInstructions,
//   ReloadInstructions,
// } from 'react-native/Libraries/NewAppScreen';
//
// const App = () => {
//   return (
//     <Fragment>
//       <StatusBar barStyle="dark-content" />
//       <SafeAreaView>
//         <ScrollView
//           contentInsetAdjustmentBehavior="automatic"
//           style={styles.scrollView}>
//           <Header />
//           {global.HermesInternal == null ? null : (
//             <View style={styles.engine}>
//               <Text style={styles.footer}>Engine: Hermes</Text>
//             </View>
//           )}
//           <View style={styles.body}>
//             <View style={styles.sectionContainer}>
//               <Text style={styles.sectionTitle}>Step One</Text>
//               <Text style={styles.sectionDescription}>
//                 Edit <Text style={styles.highlight}>App.js</Text> to change this
//                 screen and then come back to see your edits.
//               </Text>
//             </View>
//             <View style={styles.sectionContainer}>
//               <Text style={styles.sectionTitle}>See Your Changes</Text>
//               <Text style={styles.sectionDescription}>
//                 <ReloadInstructions />
//               </Text>
//             </View>
//             <View style={styles.sectionContainer}>
//               <Text style={styles.sectionTitle}>Debug</Text>
//               <Text style={styles.sectionDescription}>
//                 <DebugInstructions />
//               </Text>
//             </View>
//             <View style={styles.sectionContainer}>
//               <Text style={styles.sectionTitle}>Learn More</Text>
//               <Text style={styles.sectionDescription}>
//                 Read the docs to discover what to do next:
//               </Text>
//             </View>
//             <LearnMoreLinks />
//           </View>
//         </ScrollView>
//       </SafeAreaView>
//     </Fragment>
//   );
// };
//
// const styles = StyleSheet.create({
//   scrollView: {
//     backgroundColor: Colors.lighter,
//   },
//   engine: {
//     position: 'absolute',
//     right: 0,
//   },
//   body: {
//     backgroundColor: Colors.white,
//   },
//   sectionContainer: {
//     marginTop: 32,
//     paddingHorizontal: 24,
//   },
//   sectionTitle: {
//     fontSize: 24,
//     fontWeight: '600',
//     color: Colors.black,
//   },
//   sectionDescription: {
//     marginTop: 8,
//     fontSize: 18,
//     fontWeight: '400',
//     color: Colors.dark,
//   },
//   highlight: {
//     fontWeight: '700',
//   },
//   footer: {
//     color: Colors.dark,
//     fontSize: 12,
//     fontWeight: '600',
//     padding: 4,
//     paddingRight: 12,
//     textAlign: 'right',
//   },
// });
//
// export default App;
