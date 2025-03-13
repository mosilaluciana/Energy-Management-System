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
import PersonForm from "./components/person-form";
import PersonUpdateForm from "./components/person-update";

import * as API_USERS from "./api/person-api";
import PersonTable from "./components/person-table";
import {deleteUserWithDevices} from "../device/api/device-api";

class PersonContainer extends React.Component {
    constructor(props) {
        super(props);
        this.toggleForm = this.toggleForm.bind(this);
        this.reload = this.reload.bind(this);
        this.handleUpdateClick = this.handleUpdateClick.bind(this);
        this.handleDeleteClick = this.handleDeleteClick.bind(this);

        this.state = {
            selectedPerson: null,
            showModal: false,
            tableData: [],
            isLoaded: false,
            errorStatus: 0,
            error: null,
            isUpdating: false
        };
    }

    componentDidMount() {
        this.fetchPersons();
    }

    fetchPersons() {
        return API_USERS.getPersons((result, status, err) => {
            if (result !== null && status === 200) {
                this.setState({
                    tableData: result,
                    isLoaded: true
                });
            } else {
                this.setState({
                    errorStatus: status,
                    error: err
                });
            }
        });
    }


    toggleForm() {
        this.setState(prevState => ({
            showModal: !prevState.showModal,
            selectedPerson: null, // Reset on toggle
            selectedAddress: null, // Reset selectedAddress on toggle
            isUpdating: false
        }));
    }

    reload() {
        this.setState({
            isLoaded: false
        });
        //this.toggleForm();
        this.fetchPersons();
    }

    handleUpdateClick(person) {
        this.setState({
            selectedPerson: person,
            showModal: true,
            isUpdating: true // Set to true when updating
        });
    }



    handleDeleteClick(address) {
        // Set the selected address to state for confirmation
        this.setState({ selectedAddress: address }, this.confirmDelete);
    }


    confirmDelete() {
        const { selectedAddress } = this.state;

        if (selectedAddress && window.confirm("Are you sure you want to delete this person?")) {
            // Apelăm funcția deleteUserWithDevices pentru a șterge atât dispozitivele, cât și utilizatorul
            deleteUserWithDevices(selectedAddress, (success, status, error) => {
                if (success) {
                    this.reload(); // Reîncărcăm lista după ștergere
                    this.setState({ selectedAddress: null }); // Resetăm selectedAddress
                } else {
                    console.error("Eroare în timpul ștergerii:", error);
                    this.setState({
                        errorStatus: status || 500, // Setăm statusul de eroare
                        error: error || "An unexpected error occurred", // Setăm mesajul de eroare
                    });
                }
            });
        } else {
            // Resetăm selectedAddress dacă utilizatorul anulează ștergerea
            this.setState({ selectedAddress: null });
        }
    }





    // confirmDelete() {
    //     const { selectedAddress } = this.state;
    //
    //     if (selectedAddress && window.confirm("Are you sure you want to delete this person?")) {
    //         API_USERS.deletePersonByAddress(selectedAddress) // Call the promise-based function
    //             .then(() => {
    //                 this.reload(); // Refresh the list after deletion
    //                 this.setState({ selectedAddress: null }); // Reset selectedAddress
    //             })
    //             .catch(error => {
    //                 console.error("Error during deletion:", error);
    //                 this.setState({
    //                     errorStatus: error.response ? error.response.status : 500,
    //                     error: error.message,
    //                 });
    //             });
    //     } else {
    //         this.setState({ selectedAddress: null }); // Reset selectedAddress if the user cancels
    //     }
    // }




    // handleRowSelect = (person) => {
    //     this.setState({ selectedPerson: person });
    // };
    //
    //

    render() {
        return (
            <div>
                <CardHeader>
                    <strong>Person Management</strong>
                </CardHeader>
                <Card>
                    <br />
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            <Button color="primary" onClick={this.toggleForm}>Add Person</Button>
                            <Button
                                color="primary"
                                onClick={() => this.handleUpdateClick(this.state.selectedPerson)}>Update Person
                            </Button>

                        </Col>
                    </Row>
                    <br />
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            {this.state.isLoaded && (
                                <PersonTable
                                    tableData={this.state.tableData}
                                    onDelete={this.handleDeleteClick} // Transmit funcția pentru sterere
                                />
                            )}
                            {this.state.errorStatus > 0 && (
                                <APIResponseErrorMessage
                                    errorStatus={this.state.errorStatus}
                                    error={this.state.error}
                                />
                            )}
                        </Col>
                    </Row>

                </Card>

                <Modal isOpen={this.state.showModal} toggle={this.toggleForm}
                       className={this.props.className} size="lg">
                    <ModalHeader toggle={this.toggleForm}>
                        {this.state.isUpdating ? "Update Person" : "Add Person"}
                    </ModalHeader>
                    <ModalBody>
                        {this.state.isUpdating ? (
                            <PersonUpdateForm
                                reloadHandler={this.reload}
                                person={this.state.selectedPerson} // Transmite datele persoanei selectate
                                isUpdating={this.state.isUpdating} // Indică dacă suntem în modul de actualizare
                            />
                        ) : (
                            <PersonForm
                                reloadHandler={this.reload}
                            />
                        )}
                    </ModalBody>
                </Modal>
            </div>
        );
    }
}
export default PersonContainer;