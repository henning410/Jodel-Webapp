import React from "react";
import clsx from 'clsx';
import { withStyles } from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import InputAdornment from '@material-ui/core/InputAdornment';
import FormControl from '@material-ui/core/FormControl';
import Button from '@material-ui/core/Button';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import MuiAlert from '@material-ui/lab/Alert';
import axios from "axios";
import GoogleLogin from 'react-google-login';
import Logout from './Logout';
import Snackbar from '@material-ui/core/Snackbar';
import ArrowForwardIcon from '@material-ui/icons/ArrowForward';

const clientId = 'INSERT GOOGLE CLIENT ID HERE';


const styles = theme => ({
	center: {
		display: 'flex',
		justifyContent: 'center',
		alignItems: 'center',
		minHeight: '100vh',
	},
	loginbox: {
		textAlign: 'center',
		width: '50ch',
		border: '1px solid gray',
		borderRadius: '3px',
		padding: '10px',
	},
	buttonRightAlign: {
		padding: '20px',
		textAlign: 'right',
	},
	margin: {
	  margin: theme.spacing(1),
	},
	withoutLabel: {
	  marginTop: theme.spacing(3),
	},
	textField: {
	  width: '40ch',
	  padding: '1px',
	},
	errorText: {
		color: 'red',
	},
	signUpDiv: {
		marginTop: '10vh'
	}
});

function Alert(props) {
	return <MuiAlert elevation={6} variant="filled" {...props} />;
  }

class Login extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
			password: '',
			username: '',
			showPassword: false,
			loginButtonDisabled: true,
			showError: false,
			user: [],
			created: "",
		};		
	}
	
	handleChange = (event) => {
	const username = this.state.username;
	  if (event.target.id==="username") {
		this.setState({username: event.target.value});
		if (username !== null && username.length < 2) {
			this.setState({loginButtonDisabled: true });
		} else {
			this.setState({loginButtonDisabled: false });
		}
	  }
	  if (event.target.id==="password") {
		this.setState({password: event.target.value});
	  }
	};
  
	handleClickShowPassword = () => {
	  this.setState({showPassword: !this.state.showPassword });
	};

	status( response ) {
        if ( response.status >= 200 && response.status < 300 ) {
            return Promise.resolve( response )
        } else {
            return Promise.reject( new Error( response.statusText ) )
        }
    }

    processData = ( data ) => {
		let tid = data.id;
		if ( tid !== 0 ) {
			this.props.authorized(this.state.user);
		}
    }

	setErrorText = () =>{
		this.setState({showError: true});
	}

	signUpClicked = () => {
		console.log("sign up geklickt");
		this.props.signUp(true);
	}

	handleLoginSubmit = ( event ) => {
        var formdata = JSON.stringify( this.state );
        fetch( this.props.url + "users/login", {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            method: 'post',
            body: formdata
        })
            .then( this.status )
            .then((response) =>{ 
				if(response.status!=200){
					this.setErrorText();
				}
				return response.json() } )
            .then( this.processData )
            .catch( function( error ) {
                console.log( 'Request failed', error );
            });

		//axios to get the logged in user
		axios({
			method: 'post',
			headers: {
				'Content-Type': 'application/json',
				'Accept': 'application/json',
			},
			url: this.props.url + 'users/login',
			data: formdata
		})
		.then((response)=>{
			this.setState({user: response.data})
		})
		.catch((error) =>{
			console.log(error);
		});
		event.preventDefault();
    }

	onSuccess = (res) => {
		console.log('[Login Success] currentUser:',  res.profileObj);
		//check if GoogleUser already exists in our DB
		axios.get(this.props.url + "users/getByGoogleId?id=" + res.profileObj.googleId)
			.then((response) => {
				console.log("Google User: " , response.data);
				this.setState({ user: response.data});
				//when there is no user with that googleID in our database we create one
				if(!response.data) {
						axios.post(this.props.url + "users/createUser?googleId=" + res.profileObj.googleId + '&username=' + res.profileObj.givenName)
						.then((response) => {
							console.log("Google User erstellt: " , response.data);
							this.setState({ user: response.data});
						})
						.catch((error) => {
						console.log(error);
						});
				}
				//after setting our user to state we call authorized callback function. We need to wait a little bit because setting state takes some time
				setTimeout(() => {
					this.props.authorized(this.state.user)
				}, 100);
			})
			.catch((error) => {
				console.log(error);
			});
	}

	onFailure = (res) => {
		console.log('[Login failed] res:',  res);
	}

	componentDidMount(){
		this.setState({created: false});
	}

	render() {
		const { classes } = this.props;
		return (
		<div className={classes.center}>
			<div className={classes.loginbox}>
			<form onSubmit={this.handleLoginSubmit}>
				<FormControl className={clsx(classes.margin, classes.textField)}>
					<InputLabel htmlFor="login">Username</InputLabel>
					<Input
					id="username"
					type='text'
					value={this.state.username}
					onChange={this.handleChange}
					/>
				</FormControl>
				<FormControl className={clsx(classes.margin, classes.textField)}>
					<InputLabel htmlFor="password">Password</InputLabel>
					<Input id="password"
					type={this.state.showPassword ? 'text' : 'password'}
					value={this.state.password}
					onChange={this.handleChange}
					endAdornment={
						<InputAdornment position="end">
						<IconButton
							aria-label="toggle password visibility"
							onClick={this.handleClickShowPassword}
							onMouseDown={this.handleMouseDownPassword}
						>
							{this.state.showPassword ? <Visibility /> : <VisibilityOff />}
						</IconButton>
						</InputAdornment>
					}
					/>
				</FormControl>
				{this.state.showError ? <Alert severity="error">Username or Password wrong!</Alert> : null}
				<div className={classes.buttonRightAlign}>
					<Button variant="contained" color="primary" type="submit" value="submit" 
							disabled={this.state.loginButtonDisabled} fullWidth={true}>
							Login
					</Button>
				</div>
				<div>
					<GoogleLogin clientId={clientId}
						buttonText="Sign in with Google"
						onSuccess={this.onSuccess}
						onFailure={this.onFailure}
						cookiePolicy={'single_host_origin'}
						style={{marginTop: '100px'}}
						isSignedIn={true}
					/>
				</div>
				<div className={classes.signUpDiv}>
					<Button onClick={this.signUpClicked}>New to HS-Jodel <ArrowForwardIcon/> Sign Up</Button>
				</div>
			</form>
				
			</div>
			<Snackbar
					autoHideDuration={4000}
					open={this.props.created}
					onClose={this.handleClose}
				>
					<Alert onClose={this.handleClose} severity="success">
          				Account successfully created
        			</Alert>
				</Snackbar>
		</div>
		);
	}
}

export default withStyles(styles)(Login);

