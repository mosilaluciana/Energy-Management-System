import React from 'react';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message";
import {
    Button,
    Card,
    CardHeader,
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Row
} from 'reactstrap';
import DeviceForm from "./components/device-form";
import DeviceUpdateForm from "./components/device-update";

import * as API_DEVICES from "./api/device-api"
import DeviceTable from "./components/device-table";




class DeviceContainer extends React.Component {

    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reload = this.reload.bind(this);
        this.handleDeleteClick = this.handleDeleteClick.bind(this);
        this.handleUpdateClick = this.handleUpdateClick.bind(this);

        this.state = {
            selected: false,
            collapseForm: false,
            tableData: [],
            isLoaded: false,
            errorStatus: 0,
            error: null
        };
    }

    componentDidMount() {
        this.fetchDevices();
    }

    fetchDevices() {
        return API_DEVICES.getDevices((result, status, err) => {

            if (result !== null && status === 200) {
                this.setState({
                    tableData: result,
                    isLoaded: true
                });
            } else {
                this.setState(({
                    errorStatus: status,
                    error: err
                }));
            }
        });
    }

    toggleForm() {
        this.setState(prevState => ({
            showModal: !prevState.showModal,
            selectedDevice: null, // Reset on toggle
            selectedID: null, // Reset selectedDevice on toggle
            isUpdating: false
        }));
    }


    reload() {
        this.setState({
           // isUpdating: false,
            isLoaded: false
        });
       // this.toggleForm();
        this.fetchDevices();
    }



    handleUpdateClick(deviceId) {
        this.setState({
            selectedID: deviceId, // Set selectedID directly
            showModal: true,
            isUpdating: true // Set to true when updating
        });
    }



    handleDeleteClick(id) {
        this.setState({ selectedID: id }, this.confirmDelete);
    }

    confirmDelete() {
        const { selectedID } = this.state;

        if (selectedID && window.confirm("Are you sure you want to delete this device?")) {
            API_DEVICES.deleteDeviceByID(selectedID)
                .then(() => {
                    this.reload(); // Refresh the list after deletion
                    this.setState({ selectedID: null }); // Reset selectedID
                })
                .catch(error => {
                    console.error("Error during deletion:", error);
                    this.setState({
                        errorStatus: error.response ? error.response.status : 500,
                        error: error.message,
                    });
                });
        } else {
            this.setState({ selectedID: null }); // Reset selectedID if the user cancels
        }
    }



    render() {
        return (
            <div>
                <CardHeader>
                    <strong> Device Management </strong>
                </CardHeader>
                <Card>
                    <br/>
                    <Row>
                        <Col sm={{size: '8', offset: 1}}>
                            <Button color="primary" onClick={this.toggleForm}>Add Device </Button>
                        </Col>
                    </Row>
                    <br/>
                    <Row>
                        <Col sm={{size: '8', offset: 1}}>
                            {this.state.isLoaded && (
                                <DeviceTable
                                    tableData = {this.state.tableData}
                                    onDelete={this.handleDeleteClick} // Transmit funcția pentru sterere
                                    onEdit={this.handleUpdateClick}
                                />
                            )}
                            {this.state.errorStatus > 0 && (
                                <APIResponseErrorMessage
                                    errorStatus={this.state.errorStatus}
                                    error={this.state.error}
                                />
                            )}
                            {this.state.errorStatus > 0 && (
                                <APIResponseErrorMessage
                                    errorStatus={this.state.errorStatus}
                                    error={this.state.error}
                                />
                            )}
                            {this.state.errorStatus === 400 && (
                                <div style={{ color: "red", marginTop: "10px" }}>
                                    Please check your input values, as there may be an issue with the data format.
                                </div>
                            )}
                        </Col>
                    </Row>
                </Card>

                <Modal isOpen={this.state.showModal} toggle={this.toggleForm}
                       className={this.props.className} size="lg">
                    <ModalHeader toggle={this.toggleForm}>
                        {this.state.isUpdating ? "Update Device" : "Add Device"}
                    </ModalHeader>
                    <ModalBody>
                        {this.state.isUpdating ? (
                            <DeviceUpdateForm
                                reloadHandler={this.reload}
                                selectedID={this.state.selectedID} // Pass selected ID here
                                isUpdating={this.state.isUpdating}
                            />
                        ) : (
                            <DeviceForm
                                reloadHandler={this.reload}
                            />
                        )}
                    </ModalBody>
                </Modal>
            </div>
        )

    }
}


export default DeviceContainer;
