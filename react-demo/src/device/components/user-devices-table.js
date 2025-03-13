// components/device-table.js

import React from "react";
import Table from "../../commons/tables/table";

const filters = [
    {
        accessor: 'description',
    }
];

class DeviceTable extends React.Component {
    render() {
        const columns = [
            {
                Header: 'ID',
                accessor: 'id',
            },
            {
                Header: 'Description',
                accessor: 'description',
            },
            {
                Header: 'Address',
                accessor: 'address',
            },
            {
                Header: 'Max Hourly Energy Consumption',
                accessor: 'maxHrEnCon',
            },
        ];

        return (
            <Table
                data={this.props.tableData}
                columns={columns}
                search={filters}
                pageSize={5}
                onRowClick={(row) => this.props.selectHandler(row.original)}
            />
        );
    }
}

export default DeviceTable;
