import React from 'react';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message";
import { Button, Card, CardHeader, Col, Row } from 'reactstrap';
import DeviceTable from "./components/user-devices-table";
import * as API_DEVICES from "./api/device-api";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";

class UserDeviceContainer extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            tableData: [],
            selectedDate: null,
            chartData: null, // Datele pentru grafic
            isLoaded: false,
            errorStatus: 0,
            error: null,
        };

        this.handleDateChange = this.handleDateChange.bind(this);
        this.sendDateAndString = this.sendDateAndString.bind(this);
    }

    componentDidMount() {
        this.fetchDevices(this.props.userId); // Fetch devices for the specific user
    }

    fetchDevices(userId) {
        API_DEVICES.getDevicesForCurrentUser((result, status, error) => {
            if (status === 200 && result) {
                this.setState({
                    tableData: result,
                    isLoaded: true,
                    errorStatus: null,
                    error: null,
                });
            } else {
                this.setState({
                    errorStatus: status || 500,
                    error: error || "An unexpected error occurred",
                });
            }
        });
    }

    handleDateChange(event) {
        this.setState({ selectedDate: event.target.value });
    }

    sendDateAndString() {
        const { selectedDate } = this.state;
        const stringToSend = localStorage.getItem("userId");

        if (selectedDate && stringToSend) {
            const dataToSend = {
                date: selectedDate, // Data selectată
                string: stringToSend, // UserId obținut din localStorage
            };

            API_DEVICES.sendDateAndString(dataToSend, (result, status, error) => {
                if (status === 200) {
                    // Actualizăm chartData cu răspunsul primit
                    this.setState({ chartData: result });
                } else {
                    this.setState({
                        errorStatus: status || 500,
                        error: error || "An error occurred while fetching data",
                    });
                }
            });
        } else {
            alert("Please select a date and make sure userId is stored.");
        }
    }

    renderChart() {
        const { chartData } = this.state;

        if (!chartData) {
            return null; // Nu afișăm graficul dacă nu există date
        }

        return (
            <ResponsiveContainer width="100%" height={400}>
                <LineChart data={chartData} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="hour" label={{ value: "Hours", position: "insideBottom", offset: -5 }} />
                    <YAxis label={{ value: "Energy (kWh)", angle: -90, position: "insideLeft" }} />
                    <Tooltip />
                    <Legend />
                    <Line type="monotone" dataKey="consumption" stroke="#8884d8" activeDot={{ r: 8 }} />
                </LineChart>
            </ResponsiveContainer>
        );
    }

    render() {
        return (
            <div>
                <CardHeader>
                    <strong>User Devices Management</strong>
                </CardHeader>
                <Card>
                    <Row>
                        <Col sm={{ size: '8', offset: 1 }}>
                            {this.state.isLoaded && (
                                <DeviceTable tableData={this.state.tableData} />
                            )}
                            {this.state.errorStatus > 0 && (
                                <APIResponseErrorMessage
                                    errorStatus={this.state.errorStatus}
                                    error={this.state.error}
                                />
                            )}
                            <div style={{ marginTop: "20px" }}>
                                <input
                                    type="date"
                                    onChange={this.handleDateChange}
                                    style={{ marginRight: "10px" }}
                                />
                                <Button color="primary" onClick={this.sendDateAndString}>
                                    View historical energy consumption
                                </Button>
                            </div>
                            <div style={{ marginTop: "30px" }}>
                                {this.renderChart()}
                            </div>
                        </Col>
                    </Row>
                </Card>
            </div>
        );
    }
}

export default UserDeviceContainer;
