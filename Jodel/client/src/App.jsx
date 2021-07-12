import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Login from './Login';
import Posts from './Posts';
import Logout from './Logout';
import * as url from "./Url";
import SignUp from "./SignUp";

const styles = theme => ({
	center: {
		display: 'flex',
		justifyContent: 'center',
		alignItems: 'center',
		minHeight: '100vh',
	},
});

const theUrl =url.path;

class App extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
			loggedIn: false,
			user: '',
			signUp: false,
			created: false,
		};
	}

	authorized = (user) => {
		this.setState({loggedIn: true});
		//handing over the logged in user to our components
		this.setState({user: user})
	}

	loggingOut = () =>{
		this.setState({loggedIn: false});
		this.setState({user: ''});
	}

	signUp = (value) =>{
		this.setState({signUp: value});
	}

	created = (value) => {
		this.setState({created: value});
		console.log("CHANGE TO: ", value)
	}
  
	render() {
		if (this.state.loggedIn && this.state.user!='') {
			return (
				<div>
					<Posts url={theUrl} currentUser={this.state.user}></Posts> 
					{/* We deciced to don't use logout button because it's nicer if the user stays logged in. But
					this can be easily changed by comment out the line below */}
					{/* <Logout loggingOut={this.loggingOut}></Logout> */}
				</div>
			);
		} else if(this.state.signUp && !this.state.created) {
			return (
				<SignUp url={theUrl} signUp={this.signUp} created={this.created}></SignUp> 
				);
		} else{
			return (
				<Login url={theUrl} authorized = {this.authorized} signUp={this.signUp} created={this.state.created}></Login> 
				);
		}
	}
}

export default withStyles(styles)(App);

