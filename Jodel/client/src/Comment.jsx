import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import CommentForm from './CommentForm';
import axios from "axios";
import Geocode from "react-geocode";

const styles = theme => ({
	center: {
		display: 'flex', 
		justifyContent: 'space-between',
		alignItems: 'center',
		// minHeight: '100vh',
		border: "1px solid black",
	},
	comment: {
		paddingLeft: '10px',
	}
});

class Comment extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
            data: [],
			value: '',
			voting: [],
			childData: '',
			location: "",
		};		
	}

	handleCallback = (childData) => {
		this.setState({childData: childData});
		console.log("Daten vom Kind Post: ", childData);
		this.props.parentCallBack("Data from child CommentForm");
			setTimeout(() => {
				this.props.parentCallBack('');
			}, 100);
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

	componentWillMount(){
		this.getLocation(this.props.comment.latitude, this.props.comment.longitude);
		this.getVote();
	}

	getVote(){
		axios.get(this.props.url + "votings/getVoting?id=" + this.props.comment.comment_id)
		.then((response) => {
			console.log("voting list", response.data);
				this.setState({value: response.data.value})
				this.setState({voting: response.data})
		})
		.catch((error) => {
			console.log(error);
		});
	}

	componentDidUpdate(prevProps, prevState) {
		if(prevState.childData !== this.state.childData){
			this.getVote();
			this.getLocation(this.props.post.latitude, this.props.post.longitude);
		}
	}
	
  
	render() {
		const { classes } = this.props;
		return (
		<div className={classes.center}>
			<div className={classes.comment}>
				<Typography	variant="h5" a>{this.props.comment.text}</Typography>
				<Typography	variant="h7" a>{this.state.location}</Typography>
			</div>
			<CommentForm comment={this.props.comment} value={this.state.value} voting={this.state.voting} currentUser={this.props.currentUser} parentCallBack={this.handleCallback} url={this.props.url}></CommentForm>
	    </div>
		);
	}
}

export default withStyles(styles)(Comment);

