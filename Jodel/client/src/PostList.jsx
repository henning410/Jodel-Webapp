import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Post from './Post'
import axios from "axios";

const styles = theme => ({
	center: {
		display: 'block',
		// border: '5px solid blue',
		[theme.breakpoints.up("sm")]: {
			width: '50vw',
			marginLeft: "25vw",
			padding: '10px',
		},
		[theme.breakpoints.down("sm")]: {
			width: "100vw",
		},
	}
});

class PostList extends React.Component {

	constructor(props) {
		super(props);
	    this.state = {	  
            posts: [],
			update: '',
			childData: '',
			updated: '',
		};		
	}

	//method to check if a point is within a specific radius to a center point
	arePointsNear(currentLongitude, currentLatitude, checkLongitude, checkLatitude, km){
		var ky = 40000 / 360;
		var kx = Math.cos(Math.PI * currentLatitude / 180.0) * ky;
		var dx = Math.abs(currentLongitude - checkLongitude) * kx;
		var dy = Math.abs(currentLatitude - checkLatitude) * ky;
		return Math.sqrt(dx * dx + dy * dy) <= km;
	}

	addDays(date, days) {
		var result = new Date(date);
		result.setDate(result.getDate() + days);
		return result;
	}

	//on loading the component get all Posts as List 
	componentDidMount(){
		axios.get(this.props.url + "posts/getAllPosts")
		.then((response) => {
			//Loop over all posts and only get those, where distance is max. 10km. We are assuming the surface of the earth as flat,
			//to keep calculation simplier
			if("geolocation" in navigator){
				navigator.geolocation.getCurrentPosition((position) => {
					console.log("Long: ", position.coords.longitude, "Lat: ", position.coords.latitude);
					console.log("Postlist: ", response.data)
					response.data.forEach(element =>{
						// let latitude = 52.068578992453254;
						// let longitude = 8.46197772962304;
						// if(this.arePointsNear(longitude, latitude, element.longitude, element.latitude, 30)){

						if(this.arePointsNear(position.coords.longitude, position.coords.latitude, element.longitude, element.latitude, 500)){
							console.log("Post ist im Radius: ", element);
							this.setState(state =>{
								const posts = state.posts.concat(element);
		
								return {
									posts,
								};
							});
						}else {
							console.log("Post ist nicht Radius: ", element);
						}
					})
				})
			}else{
				this.setState({posts: response.data});
			}
		})
		.catch((error) => {
			 console.log(error);
		});
	}

	handleCallback = (childData) => {
		this.setState({childData: childData});
		console.log("Daten vom Kind Post: ", childData);
	}

	componentDidUpdate(prevProps, prevState) {
		if(prevState.update !== this.props.update || prevState.childData !== this.state.childData){
			console.log("Liste wurde geändert!!");
			axios.get(this.props.url + "posts/getAllPosts")
			.then((response) => {
				//Loop over all posts and only get those, where distance is max. 10km. We are assuming the surface of the earth as flat,
				//to keep calculation simplier
				if("geolocation" in navigator){
					navigator.geolocation.getCurrentPosition((position) => {
						this.setState({posts: []});	//clear list of posts to rerender them
						response.data.forEach(element =>{
							// let latitude = 52.068578992453254;
							// let longitude = 8.46197772962304;
							//  if(this.arePointsNear(longitude, latitude, element.longitude, element.latitude, 30)){

							if(this.arePointsNear(position.coords.longitude, position.coords.latitude, element.longitude, element.latitude, 500)){
								console.log("Post ist im Radius: ", element);
								this.setState(state =>{
									const posts = state.posts.concat(element);
			
									return {
										posts,
									};
								});
								this.setState({updated: 'changed'})
								setTimeout(()=> {
									this.setState({updated: ''})
								}, 1);
							}else {
								console.log("Post ist nicht Radius: ", element);
							}
						})
					})
				}else{
					this.setState({posts: response.data});
					this.setState({updated: 'changed'})
					setTimeout(()=> {
						this.setState({updated: ''})
					}, 1);
				}
			})
			.catch((error) => {
				console.log(error);
			});
		}
	}

	render() {
		const { classes } = this.props;
		return (
		<div className={classes.center}>
			{/* <h2>This component renders a list of Post components</h2> */}
			{/* Loop over the list of posts and create for each post the post-Component */}
			{this.state.posts.map((post, index) => (
				<Post post={post} user={post.user} key={index} currentUser={this.props.currentUser} parentCallBack={this.handleCallback} updated={this.state.updated} url={this.props.url}></Post>
			))}
	    </div>
		);
	}
}

export default withStyles(styles)(PostList);

