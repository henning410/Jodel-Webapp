import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Comments from './Comments';
import PostForm from './PostForm';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import CardHeader from '@material-ui/core/CardHeader';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import axios from "axios";
import Geocode from "react-geocode";
import PlaceIcon from '@material-ui/icons/Place';

// set Google Maps Geocoding API for purposes of quota management. Its optional but recommended.
Geocode.setApiKey("INSERT GOOGLE API KEY HERE");

// set response language. Defaults to english.
Geocode.setLanguage("en");

// set response region. Its optional.
// A Geocoding request with region=es (Spain) will return the Spanish city.
Geocode.setRegion("de");

const styles = theme => ({
	root: {
		backgroundColor: "gray",
		color: "white",
	},
	center: {
		display: 'block',
		// border: '5px solid green',
		padding: '10px',
	}
});

class Post extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
            post: {},
			user: {},
			username: "",
			location: "",
			initials: "",
			childData: null,
		};		
	}

	getPost = ( event ) => {
		
		event.preventDefault();
    }

	getLocation(latitude, longitude){
		// Get address from latitude & longitude.
		Geocode.fromLatLng(latitude, longitude)
		.then((response) => {
			let city;
			for (let i = 0; i < response.results[0].address_components.length; i++) {
				    for (let j = 0; j < response.results[0].address_components[i].types.length; j++) {
				      switch (response.results[0].address_components[i].types[j]) {
				        case "locality":
				          city = response.results[0].address_components[i].long_name;
				          break;
				        case "administrative_area_level_1":
				          break;
				        case "country":
				          break;
				        default:
				          break;
				      }
				    }
			}
			this.setState({location: city});
		}, (error) => {
			console.error(error);
			}
		);


		 this.setState({location: "Esslingen am Neckar, Deutschland"});
	}

	getInitials(username){
		let temp = username.charAt(0) + username.charAt(2);
		this.setState({initials: temp.toUpperCase()})
	}

	componentDidMount(){
		this.getLocation(this.props.post.latitude, this.props.post.longitude);
		this.getInitials(this.props.user.username);
	}

	handleCallback = (childData) => {
		this.setState({data: childData});
		console.log("Daten vom Kind: ", childData);
		this.props.parentCallBack("Data from child");
		setTimeout(() => {
			this.props.parentCallBack('');
		}, 1);
	}
	
	render() {
		const { classes } = this.props;
		return (
		<div className={classes.center}>
			{/* <h3>This component renders a single post</h3> */}
			<Card className={classes.root} variant="outlined">
				<CardHeader
					avatar={
					<Avatar aria-label="recipe" className={classes.avatar}>
						{this.state.initials}
					</Avatar>
					}
					action={
					<IconButton aria-label="settings">
						<MoreVertIcon />
					</IconButton>
					}
					title={this.props.user.username}
					subheader={this.props.post.postdate}
				/>
				{/* <Typography variant="h4"> {this.state.post.user.username} </Typography> */}
      			<CardContent>
					<Typography variant="h4" component="h2">
						{this.props.post.text}
					</Typography>
					<Typography variant="body2" component="p">
						{/* {this.props.post.location} */}
						<PlaceIcon></PlaceIcon>{this.state.location}
					</Typography>
				</CardContent>
				<Comments post={this.props.post} updated={this.props.updated} currentUser={this.props.currentUser} url={this.props.url}></Comments>
            	<PostForm post={this.props.post} currentUser={this.props.currentUser} parentCallBack= {this.handleCallback} url={this.props.url}></PostForm>
			</Card>
	    </div>
		);
	}
}

export default withStyles(styles)(Post);

