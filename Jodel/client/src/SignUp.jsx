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
import ArrowBackIcon from '@material-ui/icons/ArrowBack';

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

class SignUp extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
			password: '',
			username: '',
			showPassword: false,
			loginButtonDisabled: true,
			showError: false,
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

	setErrorText = () =>{
		this.setState({showError: true});
	}

	handleLoginSubmit = ( event ) => {
		console.log("TEST");
        axios.post(this.props.url + "users/signUp?username=" + this.state.username + "&password=" + this.state.password)
			.then((response) => {
				console.log("User erstellt: ", response.data);
				this.props.signUp(false);
				this.props.created(true);
			})
			.catch((error) => {
				this.setErrorText();
			})
    }

	goBack = () => {
		this.props.signUp(false);
	}

	render() {
		const { classes } = this.props;
		return (
		<div className={classes.center}>
			<div className={classes.loginbox}>
			<form>
				<FormControl className={clsx(classes.margin, classes.textField)}>
					<InputLabel htmlFor="login" helperText={"this.state.errorText"}>Username</InputLabel>
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
				{this.state.showError ? <Alert severity="error">Username exists already!</Alert> : null}
				<div className={classes.buttonRightAlign}>
					<Button variant="contained" color="primary" onClick={this.handleLoginSubmit}
							disabled={this.state.loginButtonDisabled} fullWidth={true}>
							Sign Up
					</Button>
				</div>
			</form>
				<div className={classes.signUpDiv}>
					<Button onClick={this.goBack}><ArrowBackIcon/>Go Back</Button>
			</div>
			</div>
			
		</div>
		);
	}
}

export default withStyles(styles)(SignUp);

