import React, { Component } from 'react';
import './App.css';
import Header from './HeaderComponent';

class App extends Component {

  state = {
    xmlFile: '',
    file: '',
    error: '',
    msg: '',
    files: []
  }

  uploadFile = (event) => {
    event.preventDefault();
    this.setState({ error: '', msg: '' });

    if (!this.state.files) {
      this.setState({ error: 'Please upload a file.' })
      return;
    }

    if (this.state.files.size >= 2000000) {
      this.setState({ error: 'File size exceeds limit of 2MB.' })
      return;
    }

    let data = new FormData();
    this.state.files.forEach(file => {
      data.append('files', file);
    });

    fetch('http://localhost:8080/api/files', {
      method: 'POST',
      body: data
    }).then(response => {
      this.setState({ error: '', msg: 'Sucessfully uploaded file' });
    }).catch(err => {
      this.setState({ error: err });
    });

  }

  downloadRandomImage = () => {
    fetch('http://localhost:8080/api/files')
      .then(response => {
        const filename = response.headers.get('Content-Disposition').split('filename=')[1];
        response.blob().then(blob => {
          let url = window.URL.createObjectURL(blob);
          let a = document.createElement('a');
          a.href = url;
          a.download = filename;
          a.click();
        });
      });
  }

  onFileChange = (event) => {
    this.state.files.push(event.target.files[0]);
    // this.setState({
    //   file: event.target.files[0]
    // });
  }

  onXmlFileUpload = (event) => {
    this.state.files.push(event.target.files[0]);
    // event.preventDefault();
    // let data = new FormData();
    // data.append('file', event.target.files[0]);

    // fetch('http://localhost:8080/api/files/file1', {
    //   method: 'POST',
    //   body: data
    // }).then(response => {
    //   this.setState({ error: '', msg: 'Sucessfully uploaded file' });
    // }).catch(err => {
    //   this.setState({ error: err });
    // });
  }

  render() {
    return (
      <div>
        <Header />
        <div className="search-box-background">
          <div className="search-box">
            <h3 style={{ marginLeft: '20%', marginTop: '3%' }}>Upload a file</h3>
            <h4 style={{ color: 'red' }}>{this.state.error}</h4>
            <h4 style={{ color: 'green' }}>{this.state.msg}</h4>
            <label> XML File :
            <input onChange={this.onXmlFileUpload} type="file"></input>
            </label>
            <lable> XSL File :
            <input onChange={this.onFileChange} type="file"></input>
            </lable>
            <button style={{ marginLeft: '30%', marginTop: '5%' }} onClick={this.uploadFile}>Upload</button>
            <h3 style={{ marginLeft: '15%', marginTop: '3%' }} >Download a random file</h3>
            <button style={{ marginLeft: '30%', marginTop: '3%' }} onClick={this.downloadRandomImage}>Download</button>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
